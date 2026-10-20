package twilightforest.world.components.feature.legacy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class NoneFeatureConfiguration implements FeatureConfiguration {
	public static final NoneFeatureConfiguration INSTANCE = new NoneFeatureConfiguration();
	public static final Codec<NoneFeatureConfiguration> CODEC = MapCodec.unitCodec(INSTANCE);
}
