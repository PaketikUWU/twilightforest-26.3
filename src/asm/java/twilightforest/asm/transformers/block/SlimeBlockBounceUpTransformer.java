package twilightforest.asm.transformers.block;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.BlockHooks#stopBouncing}
 */
public final class SlimeBlockBounceUpTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("slime_block_bounce_up");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		// 26.3 moved slime bouncing into Entity; NeoForge routes the bounciness of the block below through this method
		ASMUtil.findInstructions(node, Opcodes.DRETURN).toList().forEach(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"stopBouncing",
					"(DLjava/lang/Object;)D",
					false
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.neoforged.neoforge.common.extensions.IEntityExtension",
			"getBlockBounciness",
			"(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)D"
		));
	}
}