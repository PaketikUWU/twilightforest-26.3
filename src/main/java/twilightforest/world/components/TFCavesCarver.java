package twilightforest.world.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarverOutput;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.function.Supplier;

/**
 * Framework taken from 26.1's CaveWorldCarver, everything worth knowing is documented for easier changes in the future.
 * <p/>
 * 26.3 carvers only mark a carving mask that the chunk generator fills afterward. TF's caves need to see and replace the
 * surrounding blocks (dirt roofs, highland walls, keeping away from water), so {@link twilightforest.asmhooks.WorldgenHooks#carve}
 * routes them to {@link #carveIntoChunk}, which edits the chunk directly like 26.1 did. {@link #carve} only marks the mask as a fallback.
 */
public class TFCavesCarver implements WorldCarver {

	private final Configuration config;
	private final Supplier<MapCodec<TFCavesCarver>> codec;
	private final boolean isHighlands;
	private final CarverWallProvider wallBlocks;
	private final PerlinNoise noise;

	public TFCavesCarver(Configuration config, Supplier<MapCodec<TFCavesCarver>> codec, boolean isHighlands, CarverWallProvider wallBlocks) {
		this.config = config;
		this.codec = codec;
		this.wallBlocks = wallBlocks;
		this.isHighlands = isHighlands;

		// Since this object is constructed on game bootup instead of world creation, we can't use world seed
		// Making a different random one each bootup will only screw up determinism, and across separate sessions chunkgen will produce discontinuous noise edges
		// We only need the noise for one thing anyway, and that's placing dirt
		this.noise = new PerlinNoise(new LegacyRandomSource(6972119253061020355L));
	}

	public static MapCodec<TFCavesCarver> codec(Supplier<MapCodec<TFCavesCarver>> self, boolean isHighlands, CarverWallProvider wallBlocks) {
		return Configuration.CODEC.fieldOf("config").xmap(config -> new TFCavesCarver(config, self, isHighlands, wallBlocks), TFCavesCarver::config);
	}

	public Configuration config() {
		return this.config;
	}

	@Override
	public MapCodec<? extends WorldCarver> codec() {
		return this.codec.get();
	}

	@Override
	public boolean isStartChunk(RandomSource rand) {
		// Highland caves instead spawn when the troll caves structure is nearby, and with special location ruls
		return this.isHighlands || rand.nextFloat() <= this.config.probability();
	}

	@Override
	public boolean carve(WorldGenerationContext context, RandomSource random, ChunkPos chunkPos, ChunkPos sourceChunkPos, CarverOutput output) {
		return this.carve(new Target(context, chunkPos, output, null), random, sourceChunkPos);
	}

	public boolean carveIntoChunk(WorldGenerationContext context, RandomSource random, ChunkPos sourceChunkPos, ChunkAccess access, NoiseChunk noiseChunk, RandomState randomState, MaterialRule materialRule) {
		return this.carve(new Target(context, access.getPos(), new CarvedMask(context.getMinGenY()), new ChunkTarget(access, noiseChunk, randomState, materialRule)), random, sourceChunkPos);
	}

	private boolean carve(Target target, RandomSource random, ChunkPos accessPos) {
		if (this.isHighlands && (Mth.clamp(LegacyLandmarkPlacements.manhattanDistanceFromLandmarkCenter(accessPos.x(), accessPos.z()), 0, 0b11) & 0b1) == 1)
			return false; // If highlands, enforces a binary grid (diagonal range of 4 chunks) of possible placements around the structure center, with center being one of the zero tiles

		int i = SectionPos.sectionToBlockCoord(this.getRange() * 2 - 1);

		// If highlands, only roll chance to generate even 1 cave. Otherwise, limited caves spawn for regular TF underground
		int caveCount = this.isHighlands ? random.nextInt(2) : random.nextInt(this.getCaveBound());

		for (int caveIndex = 0; caveIndex < caveCount; ++caveIndex) {
			double x = accessPos.getBlockX(random.nextInt(16));
			double y = this.config.y().sample(random, target.context());
			double z = accessPos.getBlockZ(random.nextInt(16));
			double horiz = this.config.horizontalRadiusMultiplier().sample(random);
			double vert = this.config.verticalRadiusMultiplier().sample(random);
			double floor = this.config.floorLevel().sample(random);
			CarveSkipChecker checker = (dX, dY, dZ, yPos) -> shouldSkip(dX, dY, dZ, floor);
			int tunnelCount = 1;
			if (this.isHighlands || random.nextInt(4) == 0) {
				double horizToVertRatio = this.config.yScale().sample(random);
				float radius = 1.0F + random.nextFloat() * 6.0F;
				this.createRoom(target, x, y, z, radius, horizToVertRatio, checker);
				tunnelCount += random.nextInt(4);
			}

			for (int tunnelIndex = 0; tunnelIndex < tunnelCount; ++tunnelIndex) {
				float randomRadian = random.nextFloat() * ((float) Math.PI * 2F);
				float randomPitch = (random.nextFloat() - 0.5F) / 4.0F;
				float thickness = this.getThickness(random);
				int branchCount = i - random.nextInt(i / 4);

				this.createTunnel(target, random.nextLong(), x, y, z, horiz, vert, thickness, randomRadian, randomPitch, 0, branchCount, this.getYScale(), checker);
			}
		}

		return true;
	}

	// [VanillaCopy] 26.1 WorldCarver.carveEllipsoid
	private boolean carveEllipsoid(Target target, double x, double y, double z, double horizontalRadius, double verticalRadius, CarveSkipChecker skipChecker) {
		ChunkPos chunkPos = target.chunkPos();
		double centerX = chunkPos.getMiddleBlockX();
		double centerZ = chunkPos.getMiddleBlockZ();
		double maxDelta = 16.0 + horizontalRadius * 2.0;
		if (!(Math.abs(x - centerX) > maxDelta) && !(Math.abs(z - centerZ) > maxDelta)) {
			int chunkMinX = chunkPos.getMinBlockX();
			int chunkMinZ = chunkPos.getMinBlockZ();
			int minXIndex = Math.max(Mth.floor(x - horizontalRadius) - chunkMinX - 1, 0);
			int maxXIndex = Math.min(Mth.floor(x + horizontalRadius) - chunkMinX, 15);
			int minY = Math.max(Mth.floor(y - verticalRadius) - 1, target.context().getMinGenY() + 1);
			int protectedBlocksOnTop = target.chunk() != null && target.chunk().access().isUpgrading() ? 0 : 7;
			int maxY = Math.min(Mth.floor(y + verticalRadius) + 1, target.context().getMinGenY() + target.context().getGenDepth() - 1 - protectedBlocksOnTop);
			int minZIndex = Math.max(Mth.floor(z - horizontalRadius) - chunkMinZ - 1, 0);
			int maxZIndex = Math.min(Mth.floor(z + horizontalRadius) - chunkMinZ, 15);
			boolean carved = false;
			BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

			for (int xIndex = minXIndex; xIndex <= maxXIndex; xIndex++) {
				int worldX = chunkPos.getBlockX(xIndex);
				double xd = (worldX + 0.5 - x) / horizontalRadius;

				for (int zIndex = minZIndex; zIndex <= maxZIndex; zIndex++) {
					int worldZ = chunkPos.getBlockZ(zIndex);
					double zd = (worldZ + 0.5 - z) / horizontalRadius;
					if (!(xd * xd + zd * zd >= 1.0)) {
						MutableBoolean hasGrass = new MutableBoolean(false);

						for (int worldY = maxY; worldY > minY; worldY--) {
							double yd = (worldY - 0.5 - y) / verticalRadius;
							if (!skipChecker.shouldSkip(xd, yd, zd, worldY)) {
								if (target.chunk() == null) {
									target.output().carve(xIndex, worldY, zIndex);
									carved = true;
								} else if (!((CarvedMask) target.output()).isCarved(xIndex, worldY, zIndex)) {
									target.output().carve(xIndex, worldY, zIndex);
									blockPos.set(worldX, worldY, worldZ);
									carved |= this.carveBlock(target.chunk(), target.context(), blockPos, hasGrass);
								}
							}
						}
					}
				}
			}

			return carved;
		} else {
			return false;
		}
	}

	private boolean carveBlock(ChunkTarget target, WorldGenerationContext context, BlockPos.MutableBlockPos posMutable, MutableBoolean isSurface) {
		ChunkAccess access = target.access();
		BlockPos pos = posMutable.immutable();
		BlockState stateBeforeReplacement = access.getBlockState(pos);
		if (stateBeforeReplacement.is(Blocks.GRASS_BLOCK) || stateBeforeReplacement.is(Blocks.MYCELIUM) || stateBeforeReplacement.is(Blocks.PODZOL) || stateBeforeReplacement.is(Blocks.DIRT_PATH)) {
			isSurface.setTrue();
		}

		//We dont want caves to go so far down you can see bedrock, so lets stop them right before
		if (pos.getY() < access.getMinY() + 6) return false;

		if (!this.canReplaceBlock(stateBeforeReplacement)) {
			return false;
		} else {
			BlockPos chunkOrigin = access.getPos().getWorldPosition();
			for (Direction facing : Direction.values()) {
				BlockPos relative = pos.relative(facing);
				if (isInsideChunk(relative, chunkOrigin) && access.getFluidState(relative).is(FluidTags.WATER)) {
					return false; // If replacing this block will expose any neighboring water, then skip the current position param
				}
			}

			BlockState blockStateToPlace = Blocks.CAVE_AIR.defaultBlockState();
			RandomSource randomFromPos = target.oreRandom().at(pos);

			if (!access.getFluidState(pos.above(2)).isEmpty()) // Sand doesn't quite generate until after the carvers, so we must look for liquid above possible sand instead
				blockStateToPlace = randomFromPos.nextBoolean() ? Blocks.ROOTED_DIRT.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState(); // normal dirt will get replaced with sand, special ones are required

			boolean blockPlaced = access.setBlockState(pos, blockStateToPlace) != null;

			Aquifer aquifer = target.noiseChunk().aquifer();
			if (aquifer.shouldScheduleFluidUpdate() && !blockStateToPlace.getFluidState().isEmpty()) {
				access.markPosForPostProcessing(pos);
			}

			if (isSurface.isTrue()) {
				BlockPos posDown = pos.relative(Direction.DOWN);
				if (access.getBlockState(posDown).is(Blocks.DIRT)) {
					target.randomState().surfaceSystem().topMaterial(
						target.materialRule(),
						target.randomState(),
						context,
						biomePos -> access.getNoiseBiome(QuartPos.fromBlock(biomePos.getX()), QuartPos.fromBlock(biomePos.getY()), QuartPos.fromBlock(biomePos.getZ())),
						access,
						target.noiseChunk().cachingSamplers(),
						posDown,
						!blockStateToPlace.getFluidState().isEmpty()
					).ifPresent(state -> {
						access.setBlockState(posDown, state);
						if (!state.getFluidState().isEmpty()) {
							access.markPosForPostProcessing(posDown);
						}
					});
				}
			}

			if (blockPlaced) this.postCarveBlock(access, pos, randomFromPos, chunkOrigin);

			return blockPlaced;
		}
	}

	private static boolean isInsideChunk(BlockPos relative, BlockPos chunkOrigin) {
		int deltaX = relative.getX() - chunkOrigin.getX();
		int deltaZ = relative.getZ() - chunkOrigin.getZ();
		return deltaX >= 0 && deltaZ >= 0 && deltaX <= 15 && deltaZ <= 15;
	}

	private void postCarveBlock(ChunkAccess access, BlockPos pos, RandomSource rand, BlockPos chunkOrigin) {
		for (Direction facing : Direction.values()) {
			BlockPos directionalRelative = pos.relative(facing);
			if (!isInsideChunk(directionalRelative, chunkOrigin)) continue;

			// FIXME Half-way configurable, would prefer to eliminate the isHighlands check entirely
			//  The rand.nextInt rolls should have some way of being set into a custom config as well

			if (this.isHighlands) {
				if (rand.nextInt(4) == 0 && this.canReplaceBlock(access.getBlockState(directionalRelative))) {
					access.setBlockState(directionalRelative, this.wallBlocks.getState(rand, directionalRelative));
				}
			} else if (facing != Direction.DOWN && (facing == Direction.UP || access.getBlockState(directionalRelative.above()).isAir() || this.checkNoiseThreshold(directionalRelative, 0.25f, 0.5f))) { //here's the code for making dirt roofs. Enjoy :)
				// Dirt is never placed below, always on roof, and typically to the sides

				BlockState neighboringBlock = access.getBlockState(directionalRelative);

				if (neighboringBlock.is(BlockTags.BASE_STONE_OVERWORLD) || neighboringBlock.getFluidState().is(FluidTags.WATER)) {
					access.setBlockState(directionalRelative, this.wallBlocks.getState(rand, directionalRelative));
				}
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private boolean checkNoiseThreshold(BlockPos pos, double posScalar, double threshold) {
		double noise = this.noise.get(pos.getX() * posScalar, pos.getY() * posScalar, pos.getZ() * posScalar);

		// Noise outputs values between -1 to 1, we must normalize it using n * 0.5 + 0.5
		return noise * 0.5 + 0.5 > threshold;
	}

	protected int getCaveBound() {
		return 4;
	}

	protected float getThickness(RandomSource rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	private void createRoom(Target target, double posX, double posY, double posZ, float radius, double horizToVertRatio, CarveSkipChecker checker) {
		double d0 = 1.5D + (double) (Mth.sin(((float) Math.PI / 2F)) * radius);
		double d1 = d0 * horizToVertRatio;
		this.carveEllipsoid(target, posX, posY, posZ, d0, d1, checker);
	}

	private void createTunnel(Target target, long seed, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, CarveSkipChecker checker) {
		RandomSource random = RandomSource.create(seed);
		int i = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;
		int minY = target.minY();

		for (int j = branchIndex; j < branchCount; ++j) {
			double horizontalRadius = 1.5D + (double) (Mth.sin((float) Math.PI * (float) j / (float) branchCount) * thickness);
			double verticalRadius = horizontalRadius * horizToVertRatio;
			float f2 = Mth.cos(pitch);
			posX += Mth.cos(yaw) * f2;

			float yShift = Mth.sin(pitch);
			// If posY nears bedrock, "slow" its descent if marching downwards
			posY += yShift > 0 || posY + yShift > minY + 10 ? yShift : yShift * 0.25f;

			posZ += Mth.sin(yaw) * f2;
			pitch = pitch * (flag ? 0.92F : 0.7F);
			pitch = pitch + f1 * 0.1F;
			yaw += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.createTunnel(target, random.nextLong(), posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, checker);
				this.createTunnel(target, random.nextLong(), posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, checker);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!WorldCarver.canReach(target.chunkPos(), posX, posZ, j, branchCount, thickness)) {
					return;
				}

				// Additional size-boosting to make wider & taller spherical rooms
				boolean shouldEnlargeSphere = posY > minY + 12 && random.nextInt(48) == 0;
				float sizeMultiplier = shouldEnlargeSphere
					? random.nextFloat() * random.nextFloat() * 2f + 1
					: 1;

				double sphereHRadius = Math.min(horizontalRadius * horizMult * sizeMultiplier, 10);
				double sphereVRadius = verticalRadius * vertMult * sizeMultiplier;
				// If side-boosting is applied, then squish the sphere's edge-steeped floor into a dish
				double sphereVRadiusLimited = shouldEnlargeSphere ? Math.min(sphereVRadius, sphereHRadius * 0.65f) : sphereVRadius;

				this.carveEllipsoid(target, posX, posY, posZ, sphereHRadius, sphereVRadiusLimited, checker);
			}
		}

	}

	private boolean canReplaceBlock(BlockState state) {
		return !state.is(BlockTags.ICE) && !state.getFluidState().is(FluidTags.WATER) && state.is(this.config.replaceable());
	}

	private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
		if (posY <= minY) {
			return true;
		} else {
			return posX * posX + posY * posY + posZ * posZ >= 1.0D;
		}
	}

	// Stands in for 26.1's per-chunk carving mask, which also stopped a block from being carved twice
	private static final class CarvedMask implements CarverOutput {
		private final int minY;
		private final it.unimi.dsi.fastutil.longs.LongOpenHashSet carved = new it.unimi.dsi.fastutil.longs.LongOpenHashSet();

		private CarvedMask(int minY) {
			this.minY = minY;
		}

		private static long key(int x, int y, int z) {
			return ((long) y << 8) | (x << 4) | z;
		}

		private boolean isCarved(int x, int y, int z) {
			return this.carved.contains(key(x, y, z));
		}

		@Override
		public void carve(int x, int y, int z) {
			this.carved.add(key(x, y, z));
		}

		@Override
		public int minY() {
			return this.minY;
		}

		@Override
		public int maxY() {
			return Integer.MAX_VALUE;
		}
	}

	private record Target(WorldGenerationContext context, ChunkPos chunkPos, CarverOutput output, @Nullable ChunkTarget chunk) {
		private int minY() {
			return this.chunk != null ? this.chunk.access().getMinY() : this.context.getMinGenY();
		}
	}

	private record ChunkTarget(ChunkAccess access, NoiseChunk noiseChunk, RandomState randomState, MaterialRule materialRule) {
		private PositionalRandomFactory oreRandom() {
			return this.randomState.getOrCreateRandomFactory(Identifier.withDefaultNamespace("ore"));
		}
	}

	/**
	 * The config of 26.1's CaveCarverConfiguration, kept with the same fields so TF's carver data is unchanged.
	 */
	public record Configuration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, HolderSet<Block> replaceable,
								FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
		public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(Configuration::probability),
			HeightProvider.CODEC.fieldOf("y").forGetter(Configuration::y),
			FloatProviders.CODEC.fieldOf("yScale").forGetter(Configuration::yScale),
			VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(Configuration::lavaLevel),
			RegistryCodecs.holderSet(Registries.BLOCK).fieldOf("replaceable").forGetter(Configuration::replaceable),
			FloatProviders.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(Configuration::horizontalRadiusMultiplier),
			FloatProviders.CODEC.fieldOf("vertical_radius_multiplier").forGetter(Configuration::verticalRadiusMultiplier),
			FloatProviders.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(Configuration::floorLevel)
		).apply(instance, Configuration::new));
	}
}
