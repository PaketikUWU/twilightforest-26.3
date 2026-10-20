package twilightforest.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFactor;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import twilightforest.TwilightForestMod;

public class TFRenderPipelines {

	public static final String AURORA_UNIFORM = "TwilightForestAurora";
	public static final BindGroupLayout AURORA_BIND_GROUP = BindGroupLayout.builder().withUniform(AURORA_UNIFORM, UniformType.UNIFORM_BUFFER).build();

	private static final BlendFunction SHADOW = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

	public static final RenderPipeline AURORA = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/aurora"))
		.withVertexShader(TwilightForestMod.prefix("core/aurora/aurora"))
		.withFragmentShader(TwilightForestMod.prefix("core/aurora/aurora"))
		.withBindGroupLayout(AURORA_BIND_GROUP)
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
		.build();

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.BLOCK_SNIPPET)
		.withLocation(TwilightForestMod.prefix("core/red_thread/red_thread"))
		.withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
		.withCull(true)
		.withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
		.build();

	public static final OitPipelineSet OIT_RED_THREAD = OitPipelineSet.builder(
			TwilightForestMod.prefix("red_thread"),
			RenderPipeline.builder(RenderPipelines.GENERIC_BLOCKS_SNIPPET)
				.withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
				.withVertexShader("core/block")
				.withFragmentShader("core/block")
				.withShaderDefine("OIT_ADDITIVE")
				.withCull(true)
		)
		.withAccumulateModifier(accumulate -> accumulate.withBindGroupLayout(BindGroupLayouts.SAMPLER2))
		.withoutDepthTest()
		.build();

	public static final RenderPipeline PROTECTION_BOX = RenderPipeline.builder(RenderPipelines.ENERGY_SWIRL_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/protection_box"))
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withDepthStencilState(DepthStencilState.DEFAULT)
		.build();

	public static final OitPipelineSet OIT_PROTECTION_BOX = OitPipelineSet.builder(
			TwilightForestMod.prefix("protection_box"),
			RenderPipeline.builder(RenderPipelines.ENERGY_SWIRL_SNIPPET)
		)
		.build();

	public static final RenderPipeline SHADOW_CLONE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/shadow_clone"))
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withBindGroupLayout(BindGroupLayouts.SAMPLER1)
		.withCull(false)
		.withColorTargetState(new ColorTargetState(SHADOW))
		.build();

	public static final OitPipelineSet OIT_SHADOW_CLONE = OitPipelineSet.builder(
			TwilightForestMod.prefix("shadow_clone"),
			RenderPipeline.builder(RenderPipelines.OIT_ENTITY_SNIPPET).withCull(false)
		)
		.withAccumulateModifier(accumulate -> accumulate.withBindGroupLayout(BindGroupLayouts.SAMPLER1).withBindGroupLayout(BindGroupLayouts.SAMPLER2))
		.build();
}
