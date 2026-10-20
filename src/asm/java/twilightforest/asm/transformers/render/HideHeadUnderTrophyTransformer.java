package twilightforest.asm.transformers.render;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.RenderHooks#applyHeadVisibility}
 */
public class HideHeadUnderTrophyTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("hide_head_under_trophy");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		// Applied right before the model is drawn (after setupAnim and NeoForge's visibility modifiers) and restored right after
		ASMUtil.streamInstructions(node)
			.filter(insn -> insn instanceof MethodInsnNode method && method.getOpcode() == Opcodes.INVOKEVIRTUAL && "net/minecraft/client/model/Model".equals(method.owner) && "renderToBuffer".equals(method.name))
			.findFirst()
			.ifPresent(target -> {
				node.instructions.insertBefore(target, ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/RenderHooks",
						"applyHeadVisibility",
						"(Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)V",
						false)
				));
				node.instructions.insert(target, ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/RenderHooks",
						"restoreHeadVisibility",
						"(Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)V",
						false)
				));
			});
	}

	@Override
	public Set<SimpleMethodProcessor.Target> targets() {
		return Set.of(new SimpleMethodProcessor.Target(
				"net.minecraft.client.renderer.feature.ModelFeatureRenderer",
				"prepareModel",
				"(Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)V"
			)
		);
	}

}
