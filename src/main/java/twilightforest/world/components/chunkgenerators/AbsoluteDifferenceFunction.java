package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public abstract class AbsoluteDifferenceFunction implements PointDensityFunction {
	public static Min min(double max, BlockPos pos) {
		return new Min(max, pos.getX(), pos.getZ());
	}

	public static Max max(double max, BlockPos pos) {
		return new Max(max, pos.getX(), pos.getZ());
	}

	protected final double max, centerX, centerZ;

	public AbsoluteDifferenceFunction(double max, double centerX, double centerZ) {
		this.max = max;
		this.centerX = centerX;
		this.centerZ = centerZ;
	}

	@Override
	public float minValue() {
		return 0;
	}

	@Override
	public float maxValue() {
		return (float) this.max;
	}

	public static class Min extends AbsoluteDifferenceFunction {
		public static final MapCodec<Min> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.DOUBLE.fieldOf("max").forGetter(f -> f.max),
			Codec.DOUBLE.fieldOf("x_center").forGetter(f -> f.centerX),
			Codec.DOUBLE.fieldOf("z_center").forGetter(f -> f.centerZ)
		).apply(instance, Min::new));

		public Min(double max, double xCenter, double zCenter) {
			super(max, xCenter, zCenter);
		}

		@Override
		public float compute(int blockX, int blockY, int blockZ) {
			return (float) Math.min(Math.min(Math.abs(blockX - this.centerX), Math.abs(blockZ - this.centerZ)), this.max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}

	public static class Max extends AbsoluteDifferenceFunction {
		public static final MapCodec<Max> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.DOUBLE.fieldOf("max").forGetter(f -> f.max),
			Codec.DOUBLE.fieldOf("x_center").forGetter(f -> f.centerX),
			Codec.DOUBLE.fieldOf("z_center").forGetter(f -> f.centerZ)
		).apply(instance, Max::new));

		public Max(double max, double xCenter, double zCenter) {
			super(max, xCenter, zCenter);
		}

		@Override
		public float compute(int blockX, int blockY, int blockZ) {
			return (float) Math.min(Math.max(Math.abs(blockX - this.centerX), Math.abs(blockZ - this.centerZ)), this.max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}
}
