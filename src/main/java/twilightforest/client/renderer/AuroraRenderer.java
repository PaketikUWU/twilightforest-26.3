package twilightforest.client.renderer;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import net.minecraft.client.renderer.DynamicGpuDataStorage;
import net.minecraft.client.renderer.DynamicGpuDataStorageMapped;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class AuroraRenderer {

	private final Supplier<DynamicGpuDataStorageMapped<AuroraUniform>> uniformStorage = Suppliers.memoize(() ->
		new DynamicGpuDataStorageMapped<>("Twilight Forest Aurora UBO", AuroraUniform.SIZE, GpuBuffer.USAGE_UNIFORM, 2)
	);

	@Nullable
	private GpuBuffer vertexBuffer;
	@Nullable
	private PreparedAurora prepared;

	public void endFrame() {
		this.uniformStorage.get().endFrame();
	}

	public void prepare(@Nullable MeshData mesh, float alpha, int seed, float x, float y, float z) {
		this.prepared = null;
		if (mesh == null)
			return;

		try (mesh) {
			MeshData.DrawState drawState = mesh.drawState();
			ByteBuffer data = mesh.vertexBuffer();
			GpuDevice device = RenderSystem.getDevice();
			if (this.vertexBuffer == null || this.vertexBuffer.size() < data.remaining()) {
				if (this.vertexBuffer != null)
					this.vertexBuffer.close();
				this.vertexBuffer = device.createBuffer(() -> "Twilight Forest aurora vertices", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, data.remaining());
			}
			device.createCommandEncoder().writeToBuffer(this.vertexBuffer.slice(), data);
			RenderSystem.getSequentialBuffer(drawState.primitiveTopology()).requestIndexCount(drawState.indexCount());
			this.prepared = new PreparedAurora(drawState, alpha, seed, x, y, z);
		}
	}

	public void render(RenderPass pass, Matrix4fc modelView) {
		PreparedAurora aurora = this.prepared;
		this.prepared = null;
		if (aurora == null || this.vertexBuffer == null)
			return;

		RenderSystem.AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(aurora.drawState().primitiveTopology());
		GpuBuffer indexBuffer = sequential.getBuffer(aurora.drawState().indexCount());
		GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
			.writeTransform(new Matrix4f(modelView), new Vector4f(1.0F, 1.0F, 1.0F, aurora.alpha()));
		GpuBufferSlice auroraUniform = this.uniformStorage.get().writeData(new AuroraUniform(aurora.seed(), aurora.x(), aurora.y(), aurora.z()));

		pass.pushDebugGroup(() -> "Aurora");
		pass.setPipeline(RenderSystem.getCompiledPipeline(TFRenderPipelines.AURORA));
		RenderSystem.bindDefaultUniforms(pass);
		pass.setUniform("DynamicTransforms", transforms);
		pass.setUniform(TFRenderPipelines.AURORA_UNIFORM, auroraUniform);
		pass.setVertexBuffer(0, this.vertexBuffer.slice());
		pass.setIndexBuffer(indexBuffer, sequential.type());
		pass.drawIndexed(aurora.drawState().indexCount(), 1, 0, 0, 0);
		pass.popDebugGroup();
	}

	private record PreparedAurora(MeshData.DrawState drawState, float alpha, int seed, float x, float y, float z) {

	}

	private record AuroraUniform(int seed, float x, float y, float z) implements DynamicGpuDataStorage.DynamicGpuData {

		private static final int SIZE = new Std140SizeCalculator().putInt().putVec3().get();

		@Override
		public void write(ByteBuffer buffer) {
			Std140Builder.intoBuffer(buffer).putInt(this.seed).putVec3(this.x, this.y, this.z);
		}

	}

}
