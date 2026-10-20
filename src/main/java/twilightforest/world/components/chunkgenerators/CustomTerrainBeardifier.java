package twilightforest.world.components.chunkgenerators;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

/**
 * Adds the terrain of TF's CustomDensitySource structures on top of the vanilla beardifier of a chunk.
 */
public class CustomTerrainBeardifier extends Beardifier {
	// Structure terraformers are built from positional functions only, they never sample noise or randoms
	private static final DensityFunction.CompileContext NO_NOISE_CONTEXT = new DensityFunction.CompileContext() {
		@Override
		public Noise createNoiseSampler(Holder<NormalNoise> parameters) {
			throw new UnsupportedOperationException("Structure terraformers cannot use noise: " + parameters);
		}

		@Override
		public RandomSource createRandom(Identifier seed) {
			throw new UnsupportedOperationException("Structure terraformers cannot use randoms: " + seed);
		}

		@Override
		@Deprecated
		public RandomSource createEndIslandRandom() {
			throw new UnsupportedOperationException("Structure terraformers cannot use randoms");
		}
	};

	private final Beardifier vanilla;
	private final List<DensitySampler> customDensities;

	public CustomTerrainBeardifier(Beardifier vanilla, List<DensityFunction> customDensities) {
		super(List.of(), List.of(), null);
		this.vanilla = vanilla;
		this.customDensities = customDensities.stream().map(function -> function.compileSampler(NO_NOISE_CONTEXT)).toList();
	}

	@Override
	public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
		this.vanilla.sampleVolume(context, outputBuffer, volume);

		for (DensitySampler customDensity : this.customDensities) {
			try (ScopedDensityBuffer buffer = context.acquireBuffer(volume)) {
				customDensity.sampleVolume(context, buffer, volume);
				for (int i = 0; i < outputBuffer.size(); i++) {
					outputBuffer.addTo(i, buffer.get(i));
				}
			}
		}
	}

	@Override
	public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
		float density = this.vanilla.sampleValue(context, blockX, blockY, blockZ);
		for (DensitySampler customDensity : this.customDensities) {
			density += customDensity.sampleValue(context, blockX, blockY, blockZ);
		}
		return density;
	}
}
