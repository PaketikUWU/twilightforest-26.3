package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.op.UnaryFunction;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction {
	public static final MapCodec<SqrtDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
	).apply(instance, SqrtDensityFunction::new));

	@Override
	public DensitySampler compileSampler(CompileContext context) {
		return new UnaryFunction.SqrtSampler(this.input.compileSampler(context));
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction input = rule.rewrite(this.input);
		return input == this.input ? this : new SqrtDensityFunction(input);
	}

	@Override
	public Interval range() {
		return Interval.pow(this.input.range(), Interval.ofExact(0.5F));
	}

	@Override
	public @Axes int domainAxes() {
		return this.input.domainAxes();
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}
}
