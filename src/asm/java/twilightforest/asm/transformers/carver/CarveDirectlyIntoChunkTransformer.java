package twilightforest.asm.transformers.carver;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.List;
import java.util.Set;

/**
 * {@link twilightforest.asmhooks.WorldgenHooks#carve}
 */
public class CarveDirectlyIntoChunkTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("carve_directly_into_chunk");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		List<MethodInsnNode> targets = ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEINTERFACE,
			"net/minecraft/world/level/levelgen/carver/WorldCarver",
			"carve",
			"(Lnet/minecraft/world/level/levelgen/WorldGenerationContext;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/CarverOutput;)Z"
		).toList();
		for (MethodInsnNode target : targets) {
			node.instructions.insertBefore(target, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1), // ChunkAccess from params
				new VarInsnNode(Opcodes.ALOAD, 3), // NoiseChunk from params
				new VarInsnNode(Opcodes.ALOAD, 4), // RandomState from params
				new VarInsnNode(Opcodes.ALOAD, 7) // MaterialRule from params
			));
			node.instructions.set(target, new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/WorldgenHooks",
				"carve",
				"(Lnet/minecraft/world/level/levelgen/carver/WorldCarver;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/CarverOutput;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/material/rule/MaterialRule;)Z",
				false
			));
		}
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator",
			"generateCarvers",
			"(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/levelgen/material/rule/MaterialRule;)V"
		));
	}

}
