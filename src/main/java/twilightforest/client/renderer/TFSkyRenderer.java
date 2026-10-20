package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import org.joml.*;
import twilightforest.TwilightForestMod;

import java.lang.Math;
import java.util.Optional;
import java.util.OptionalDouble;

public class TFSkyRenderer implements AutoCloseable {

	public static final ContextKey<Boolean> RENDER_DARK_DISC = new ContextKey<>(TwilightForestMod.prefix("render_dark_disc"));
	private final RenderSystem.AutoStorageIndexBuffer starIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
	private final GpuBuffer starBuffer;
	private final GpuBuffer topSkyBuffer;
	private final GpuBuffer bottomSkyBuffer;
	private int starIndexCount;

	public TFSkyRenderer() {
		this.starBuffer = buildStars();
		this.topSkyBuffer = buildSkyDiscBuffer("TF Top sky vertex buffer", 16.0F);
		this.bottomSkyBuffer = buildSkyDiscBuffer("TF Bottom sky vertex buffer", -16.0F);
	}

	public static void extractLevelRender(ExtractLevelRenderStateEvent event) {
		event.getRenderState().setRenderData(RENDER_DARK_DISC, shouldDarkenSky(event.getLevel(), event.getDeltaTracker().getGameTimeDeltaTicks()));
	}

	// [VanillaCopy] SkyRenderer.render's overworld branch, without sun/moon/sunrise/sunset, using our own stars at full brightness, and lowering void horizon threshold height from getHorizonHeight (63) to 0
	public boolean renderSky(LevelRenderState level, SkyRenderState sky, Matrix4fc modelmatrix, GpuBufferSlice skyFog) {
		RenderSystem.setShaderFog(skyFog);
		GpuTextureView colorTexture = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
		GpuTextureView depthTexture = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();

		try (RenderPass renderPass = RenderSystem.getDevice()
				.createCommandEncoder()
				.createRenderPass(() -> "Twilight Forest Sky", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty())) {
			RenderSystem.bindDefaultUniforms(renderPass);

			PoseStack posestack = new PoseStack();
			this.renderSkyDisc(renderPass, sky.skyColor);
			//TF: snip out sunrise and sunset coloring

			//TF: replace sun, moon, and star rendering method with our own star renderer
			//rotation is from SkyRenderer.renderSunMoonAndStars
			posestack.pushPose();
			posestack.rotateDegrees(Axis.YP, -90.0F);
			this.renderStars(renderPass, posestack);
			posestack.popPose();
			//TF: use custom height checks for the void sky as vanilla hardcodes to 63
			if (Boolean.TRUE.equals(level.getRenderData(RENDER_DARK_DISC))) {
				this.renderDarkDisc(renderPass);
			}
		}

		return true;
	}

	private static boolean shouldDarkenSky(ClientLevel level, float partialTicks) {
		return Minecraft.getInstance().player.getEyePosition(partialTicks).y - level.getMinY() < 0.0;
	}

	// [VanillaCopy] of SkyRenderer.renderSkyDisc
	private void renderSkyDisc(RenderPass renderPass, Vector3fc skyColor) {
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
			.writeTransform(RenderSystem.getModelViewMatrixCopy(), new Vector4f(skyColor, 1.0F));
		renderPass.pushDebugGroup(() -> "Sky disc");
		renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.SKY));
		renderPass.setUniform("DynamicTransforms", dynamicTransforms);
		renderPass.setVertexBuffer(0, this.topSkyBuffer.slice());
		renderPass.draw(10, 1, 0, 0);
		renderPass.popDebugGroup();
	}

	// [VanillaCopy] of SkyRenderer.renderDarkDisc
	private void renderDarkDisc(RenderPass renderPass) {
		Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushMatrix();
		modelViewStack.translate(0.0F, 12.0F, 0.0F);
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f(modelViewStack), new Vector4f(0.0F, 0.0F, 0.0F, 1.0F));
		renderPass.pushDebugGroup(() -> "Dark disc");
		renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.SKY));
		RenderSystem.bindDefaultUniforms(renderPass);
		renderPass.setUniform("DynamicTransforms", dynamicTransforms);
		renderPass.setVertexBuffer(0, this.bottomSkyBuffer.slice());
		renderPass.draw(10, 1, 0, 0);
		renderPass.popDebugGroup();
		modelViewStack.popMatrix();
	}

	//[VanillaCopy] of SkyRenderer.renderStars, using our own buffer instead. Coloring was also removed as the stars are always fully bright
	private void renderStars(RenderPass renderPass, PoseStack stack) {
		Matrix4fStack matrix = RenderSystem.getModelViewStack();
		matrix.pushMatrix();
		matrix.mul(stack.last().pose());
		GpuBuffer indices = this.starIndices.getBuffer(this.starIndexCount);
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
			.writeTransform(new Matrix4f(matrix), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F));
		renderPass.pushDebugGroup(() -> "Stars");
		renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.STARS));
		RenderSystem.bindDefaultUniforms(renderPass);
		renderPass.setUniform("DynamicTransforms", dynamicTransforms);
		renderPass.setVertexBuffer(0, this.starBuffer.slice());
		renderPass.setIndexBuffer(indices, this.starIndices.type());
		renderPass.drawIndexed(this.starIndexCount, 1, 0, 0, 0);
		renderPass.popDebugGroup();

		matrix.popMatrix();
	}

	// [VanillaCopy] of SkyRenderer.buildSkyDisc
	private static GpuBuffer buildSkyDiscBuffer(String name, float yy) {
		try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize())) {
			BufferBuilder bufferBuilder = new BufferBuilder(builder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
			float x = Math.signum(yy) * 512.0F;
			bufferBuilder.addVertex(0.0F, yy, 0.0F);

			for (int i = -180; i <= 180; i += 45) {
				bufferBuilder.addVertex(x * Mth.cos(i * (float) (Math.PI / 180.0)), yy, 512.0F * Mth.sin(i * (float) (Math.PI / 180.0)));
			}

			try (MeshData meshData = bufferBuilder.buildOrThrow()) {
				return RenderSystem.getDevice().createBuffer(() -> name, GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
			}
		}
	}

	// [VanillaCopy] of SkyRenderer.buildStars but with double the number of them
	private GpuBuffer buildStars() {
		RandomSource random = RandomSource.create(10842L);
		GpuBuffer gpuBuffer;

		try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 3000 * 4)) {
			BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);

			// TF - 1500 -> 3000
			for (int i = 0; i < 3000; ++i) {
				float f1 = random.nextFloat() * 2.0F - 1.0F;
				float f2 = random.nextFloat() * 2.0F - 1.0F;
				float f3 = random.nextFloat() * 2.0F - 1.0F;
				float f4 = 0.15F + random.nextFloat() * 0.1F;
				float f5 = Mth.lengthSquared(f1, f2, f3);
				if (!(f5 <= 0.010000001F) && !(f5 >= 1.0F)) {
					Vector3f vector3f = new Vector3f(f1, f2, f3).normalize(100.0F);
					float f6 = (float)(random.nextDouble() * (float) Math.PI * 2.0);
					Matrix3f matrix3f = new Matrix3f().rotateTowards(new Vector3f(vector3f).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-f6);
					bufferBuilder.addVertex(new Vector3f(f4, -f4, 0.0F).mul(matrix3f).add(vector3f));
					bufferBuilder.addVertex(new Vector3f(f4, f4, 0.0F).mul(matrix3f).add(vector3f));
					bufferBuilder.addVertex(new Vector3f(-f4, f4, 0.0F).mul(matrix3f).add(vector3f));
					bufferBuilder.addVertex(new Vector3f(-f4, -f4, 0.0F).mul(matrix3f).add(vector3f));
				}
			}

			try (MeshData mesh = bufferBuilder.buildOrThrow()) {
				this.starIndexCount = mesh.drawState().indexCount();
				gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "TF Stars buffer", 40, mesh.vertexBuffer());
			}
		}

		return gpuBuffer;
	}

	@Override
	public void close() {
		starBuffer.close();
		topSkyBuffer.close();
		bottomSkyBuffer.close();
	}
}
