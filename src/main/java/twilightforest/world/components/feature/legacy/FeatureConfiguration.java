package twilightforest.world.components.feature.legacy;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.stream.Stream;

public interface FeatureConfiguration {
	NoneFeatureConfiguration NONE = NoneFeatureConfiguration.INSTANCE;

	default Stream<Holder<Feature>> getSubFeatures() {
		return Stream.empty();
	}
}
