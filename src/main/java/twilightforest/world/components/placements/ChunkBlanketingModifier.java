package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

// Ideally, you should not be mixing this with other decorators unless you know what you're doing
// This litters memory with 256 block positions for each chunk. USE SPARINGLY
public class ChunkBlanketingModifier implements PlacementModifier {

	public static final MapCodec<ChunkBlanketingModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.floatRange(0.0f, 1.0f).fieldOf("integrity").forGetter(o -> o.integrity),
		Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(o -> o.heightmap),
		RegistryCodecs.holderSet(Registries.BIOME).optionalFieldOf("biome_lock").forGetter(o -> o.biomeRLOptional)
	).apply(instance, ChunkBlanketingModifier::new));

	public final float integrity;
	public final Heightmap.Types heightmap;
	public final Optional<HolderSet<Biome>> biomeRLOptional;

	public ChunkBlanketingModifier(float integrity, Heightmap.Types heightmap, Optional<HolderSet<Biome>> biomeRLOptional) {
		this.integrity = integrity;
		this.heightmap = heightmap;
		this.biomeRLOptional = biomeRLOptional;
	}

	public static ChunkBlanketingModifier addThorns(HolderSet<Biome> thorns) {
		return new ChunkBlanketingModifier(0.7f, Heightmap.Types.OCEAN_FLOOR_WG, Optional.of(thorns));
	}

	@Override
	public void modify(PlacementContext context, RandomSource random, BlockPos placement, Consumer<BlockPos> output) {
		this.getPositions(context, random, placement).forEach(output);
	}

	private Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos placement) {
		ArrayList<BlockPos> coordinates = new ArrayList<>();

		WorldGenLevel level = context.getLevel();
		ChunkAccess chunk = level.getChunk(placement);

		int chunkOriginX = chunk.getPos().getMinBlockX();
		int chunkOriginZ = chunk.getPos().getMinBlockZ();

		// 26.3 merged the surface and carver steps into terrain, so worldgen-only heightmaps are no longer kept up to date for features
		Heightmap.Types heightmap = switch (this.heightmap) {
			case OCEAN_FLOOR_WG -> Heightmap.Types.OCEAN_FLOOR;
			case WORLD_SURFACE_WG -> Heightmap.Types.WORLD_SURFACE;
			default -> this.heightmap;
		};

		for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				if (random.nextFloat() > this.integrity)
					continue;

				BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(chunkOriginX + xInChunk, chunk.getHeight(heightmap, xInChunk, zInChunk) + 1, chunkOriginZ + zInChunk);
				// the heightmap can lag behind the terrain, so climb to the first free block above the ground
				int topY = chunk.getMaxY();
				while (pos.getY() < topY && !chunk.getBlockState(pos).isAir())
					pos.move(Direction.UP);

				if (this.biomeRLOptional.isEmpty() || this.biomeRLOptional.get().contains(level.getBiome(pos))) {
					coordinates.add(pos.immutable());
				}
			}
		}

		return coordinates.stream();
	}

	@Override
	public MapCodec<ChunkBlanketingModifier> codec() {
		return CODEC;
	}
}
