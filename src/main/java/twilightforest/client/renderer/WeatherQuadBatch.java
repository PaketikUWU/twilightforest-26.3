package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects weather-style quads (rain/snow textured columns, {@link DefaultVertexFormat#PARTICLE}) grouped by texture,
 * uploads them during the prepare phase and draws them with the vanilla weather pipelines inside an existing render pass.
 * Replaces the 26.1 weather {@code RenderType} + buffer source approach.
 */
public class WeatherQuadBatch implements AutoCloseable {

	private final String label;
	private final Map<Identifier, List<Vertex>> pending = new LinkedHashMap<>();
	private final List<Draw> draws = new ArrayList<>();
	@Nullable
	private GpuBuffer vertexBuffer;
	private int indexCount;

	public WeatherQuadBatch(String label) {
		this.label = label;
	}

	public void addVertex(Identifier texture, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int light) {
		this.pending.computeIfAbsent(texture, t -> new ArrayList<>()).add(new Vertex(x, y, z, u, v, red, green, blue, alpha, light));
	}

	public boolean hasPending() {
		return !this.pending.isEmpty();
	}

	/**
	 * Uploads the queued quads and clears the queue. Must be called outside of a render pass.
	 */
	public void upload() {
		this.draws.clear();
		this.indexCount = 0;
		int vertexCount = 0;
		for (List<Vertex> vertices : this.pending.values())
			vertexCount += vertices.size();

		if (vertexCount == 0) {
			this.pending.clear();
			return;
		}

		try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(vertexCount * DefaultVertexFormat.PARTICLE.getVertexSize())) {
			BufferBuilder bufferBuilder = new BufferBuilder(builder, PrimitiveTopology.QUADS, DefaultVertexFormat.PARTICLE);
			int firstIndex = 0;
			for (Map.Entry<Identifier, List<Vertex>> entry : this.pending.entrySet()) {
				List<Vertex> vertices = entry.getValue();
				for (Vertex vertex : vertices) {
					bufferBuilder.addVertex(vertex.x(), vertex.y(), vertex.z())
						.setUv(vertex.u(), vertex.v())
						.setColor(vertex.red(), vertex.green(), vertex.blue(), vertex.alpha())
						.setLight(vertex.light());
				}
				int quadIndices = vertices.size() / 4 * 6;
				this.draws.add(new Draw(entry.getKey(), firstIndex, quadIndices));
				firstIndex += quadIndices;
			}
			this.pending.clear();

			try (MeshData mesh = bufferBuilder.build()) {
				if (mesh == null) {
					this.draws.clear();
					return;
				}

				ByteBuffer data = mesh.vertexBuffer();
				GpuDevice device = RenderSystem.getDevice();
				if (this.vertexBuffer == null || this.vertexBuffer.size() < data.remaining()) {
					if (this.vertexBuffer != null)
						this.vertexBuffer.close();
					this.vertexBuffer = device.createBuffer(() -> this.label + " Vertex Buffer", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, data.remaining());
				}
				device.createCommandEncoder().writeToBuffer(this.vertexBuffer.slice(), data);
				this.indexCount = mesh.drawState().indexCount();
				RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS).requestIndexCount(this.indexCount);
				// Textures must be loaded before the render pass opens; loading one inside it crashes in 26.3
				for (Draw draw : this.draws)
					Minecraft.getInstance().getTextureManager().getTexture(draw.texture());
			}
		}
	}

	public void render(RenderPass renderPass, RenderPipeline pipeline) {
		if (this.vertexBuffer == null || this.draws.isEmpty())
			return;

		Minecraft minecraft = Minecraft.getInstance();
		renderPass.pushDebugGroup(() -> this.label);
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
		RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
		GpuBuffer indexBuffer = autoIndices.getBuffer(this.indexCount);
		renderPass.setPipeline(RenderSystem.getCompiledPipeline(pipeline));
		RenderSystem.bindDefaultUniforms(renderPass);
		renderPass.setUniform("DynamicTransforms", dynamicTransforms);
		renderPass.setUniform("Sampler2", minecraft.gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
		renderPass.setIndexBuffer(indexBuffer, autoIndices.type());
		renderPass.setVertexBuffer(0, this.vertexBuffer.slice());
		for (Draw draw : this.draws) {
			AbstractTexture texture = minecraft.getTextureManager().getTexture(draw.texture());
			renderPass.setUniform("Sampler0", texture.getTextureView(), texture.getSampler());
			renderPass.drawIndexed(draw.indexCount(), 1, draw.firstIndex(), 0, 0);
		}
		renderPass.popDebugGroup();
	}

	public void clear() {
		this.pending.clear();
		this.draws.clear();
		this.indexCount = 0;
	}

	@Override
	public void close() {
		this.clear();
		if (this.vertexBuffer != null) {
			this.vertexBuffer.close();
			this.vertexBuffer = null;
		}
	}

	private record Vertex(float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int light) {

	}

	private record Draw(Identifier texture, int firstIndex, int indexCount) {

	}
}
