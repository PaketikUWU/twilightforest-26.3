package twilightforest.world.components.feature.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;

public record FeaturePlaceContext<FC>(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, FC config) {
}
