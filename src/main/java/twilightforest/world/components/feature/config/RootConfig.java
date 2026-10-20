package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import twilightforest.world.components.feature.legacy.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record RootConfig(BlockStateProvider blockRoot, BlockStateProvider oreRoot) implements FeatureConfiguration {
	public static final Codec<RootConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockStateProvider.DIRECT_CODEC.fieldOf("root_block").forGetter(RootConfig::blockRoot),
		BlockStateProvider.DIRECT_CODEC.fieldOf("root_ore").forGetter(RootConfig::oreRoot)
	).apply(instance, RootConfig::new));
}
