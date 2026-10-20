package twilightforest.world.components.chunkgenerators;

import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

/**
 * A leaf density function that is evaluated per block, the 26.3 equivalent of 26.1's DensityFunction.SimpleFunction.
 */
public interface PointDensityFunction extends DensityFunction {
	float compute(int blockX, int blockY, int blockZ);

	float minValue();

	float maxValue();

	@Override
	default DensitySampler compileSampler(CompileContext context) {
		return new Sampler(this);
	}

	@Override
	default DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	@Override
	default Interval range() {
		return Interval.of(this.minValue(), this.maxValue());
	}

	@Override
	default @Axes int domainAxes() {
		return ALL_AXES;
	}

	record Sampler(PointDensityFunction function) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			return this.function.compute(blockX, blockY, blockZ);
		}
	}
}
