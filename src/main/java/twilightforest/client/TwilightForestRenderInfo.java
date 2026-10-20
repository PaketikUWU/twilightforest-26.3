package twilightforest.client;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.CustomWeatherEffectRenderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFBiomes;

import java.util.Optional;

public class TwilightForestRenderInfo implements CustomSkyboxRenderer, CustomWeatherEffectRenderer {

	public static final Identifier SKY_RENDERER = TwilightForestMod.prefix("renderer");
	public static final Identifier WEATHER_RENDERER = TwilightForestMod.prefix("weather");

	@Nullable
	private TFSkyRenderer skyRenderer;

	// FIXME rewrite method under using new Effect System
//	@Override
//	public boolean isFoggyAt(int x, int y) { // true = nearFog
//		Player player = Minecraft.getInstance().player;
//
//		if (player != null) {
//			Optional<ResourceKey<Biome>> biome = player.level().getBiome(player.blockPosition()).unwrapKey();
//			if (biome.isPresent()) {
//				boolean spooky = biome.get() == TFBiomes.SPOOKY_FOREST;
//
//				if (player.position().y > 20 && !spooky) {
//					return false; // If player is above the dark forest then no need to make it so spooky. The darkwood leaves cover everything as low as y42.
//				}
//
//				return spooky || biome.get() == TFBiomes.DARK_FOREST || biome.get() == TFBiomes.DARK_FOREST_CENTER;
//			}
//		}
//
//		return false;
//
//		//Make the fog on these biomes much much darker, maybe pitch black even. Do we keep this harsher fog underground too?
//	}

	@Override
	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, GpuBufferSlice skyFog) {
		if (this.skyRenderer == null) {
			this.skyRenderer = new TFSkyRenderer();
		}
		return skyRenderer.renderSky(levelRenderState, skyRenderState, modelViewMatrix, skyFog);
	}

	@Override
	public void prepare(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, Vec3 camPos) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level != null)
			TFWeatherRenderer.prepareSnowAndRain(level, (int) (level.getGameTime() & 131071L), Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false), camPos);
	}

	@Override
	public boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, Vec3 camPos, RenderPass renderPass) {
		return TFWeatherRenderer.renderSnowAndRain(renderPass);
	}

	@Override
	public boolean renderSnowAndRainOit(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, Vec3 camPos, OitStage stage, RenderPass renderPass) {
		return TFWeatherRenderer.renderSnowAndRainOit(stage, renderPass);
	}

	@Override
	public boolean tickRain(ClientLevel level, long ticks, Camera camera) {
		return TFWeatherRenderer.tickRain(level, ticks, camera.blockPosition());
	}
}
