package twilightforest.world.components.feature.legacy;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateConfiguration implements FeatureConfiguration {
	public static final Codec<BlockStateConfiguration> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, c -> c.state).codec();
	public final BlockState state;

	public BlockStateConfiguration(BlockState state) {
		this.state = state;
	}
}
