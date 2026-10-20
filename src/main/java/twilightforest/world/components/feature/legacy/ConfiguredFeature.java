package twilightforest.world.components.feature.legacy;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.stream.Stream;

public record ConfiguredFeature<FC, F extends Feature<FC>>(F feature, FC config) implements net.minecraft.world.level.levelgen.feature.Feature {

	@Override
	public MapCodec<? extends net.minecraft.world.level.levelgen.feature.Feature> codec() {
		return this.feature.configuredCodec();
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		return this.feature.place(this.config, level, chunkGenerator, random, origin);
	}

	@Override
	public Stream<Holder<net.minecraft.world.level.levelgen.feature.Feature>> getSubFeatures() {
		return switch (this.config) {
			case FeatureConfiguration config -> config.getSubFeatures();
			case net.minecraft.world.level.levelgen.feature.Feature feature -> feature.getSubFeatures();
			default -> Stream.empty();
		};
	}

	@Override
	public String toString() {
		return "Configured: " + this.feature + ": " + this.config;
	}
}
