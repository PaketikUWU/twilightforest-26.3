package twilightforest.world.components.feature.legacy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * [VanillaCopy] of the 26.1 Feature class. 26.3 merged features and their configurations into a single {@link net.minecraft.world.level.levelgen.feature.Feature};
 * this keeps TF's feature logic untouched by having a feature act as the type and {@link ConfiguredFeature} act as the 26.3 feature.
 */
public abstract class Feature<FC> {
	private final MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec;

	public Feature(Codec<FC> codec) {
		this.configuredCodec = codec.fieldOf("config").xmap(c -> new ConfiguredFeature<>(this, c), ConfiguredFeature::config);
	}

	public MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec() {
		return this.configuredCodec;
	}

	protected void setBlock(LevelWriter level, BlockPos pos, BlockState blockState) {
		level.setBlock(pos, blockState, Block.UPDATE_ALL);
	}

	public static Predicate<BlockState> isReplaceable(TagKey<Block> cannotReplaceTag) {
		return s -> !s.is(cannotReplaceTag);
	}

	protected void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> canReplace) {
		if (canReplace.test(level.getBlockState(pos))) {
			level.setBlock(pos, state, Block.UPDATE_CLIENTS);
		}
	}

	public abstract boolean place(final FeaturePlaceContext<FC> context);

	public boolean place(FC config, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		return level.ensureCanWrite(origin) && this.place(new FeaturePlaceContext<>(level, chunkGenerator, random, origin, config));
	}

	public static boolean checkNeighbors(Function<BlockPos, BlockState> blockGetter, BlockPos pos, Predicate<BlockState> predicate) {
		BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();

		for (Direction direction : Direction.values()) {
			neighborPos.setWithOffset(pos, direction);
			if (predicate.test(blockGetter.apply(neighborPos))) {
				return true;
			}
		}

		return false;
	}

	public static boolean isAdjacentToAir(Function<BlockPos, BlockState> blockGetter, BlockPos pos) {
		return checkNeighbors(blockGetter, pos, BlockBehaviour.BlockStateBase::isAir);
	}

	protected void markAboveForPostProcessing(WorldGenLevel level, BlockPos placePos) {
		BlockPos.MutableBlockPos pos = placePos.mutable();

		for (int i = 0; i < 2; i++) {
			pos.move(Direction.UP);
			if (level.getBlockState(pos).isAir()) {
				return;
			}

			level.getChunk(pos).markPosForPostProcessing(pos);
		}
	}
}
