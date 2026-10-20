package twilightforest.client.event;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.PrepareRenderBuffersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.block.CloudBlock;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.renderer.WeatherQuadBatch;
import twilightforest.config.TFConfig;
import twilightforest.util.Vec2i;

import java.util.ArrayList;
import java.util.List;

public class CloudEvents {
	private static final List<PrecipitationRenderHelper> RENDER_HELPER = new ArrayList<>();
	private static final WeatherQuadBatch BATCH = new WeatherQuadBatch("Twilight Forest Cloud Precipitation");

	record PrecipitationRenderHelper(BlockPos cloudPos, Biome.Precipitation precipitation, float precipitationLevel, int rainOnY) {

	}

	protected static void tickWeatherEffects(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		if (!mc.isPaused()) {
			if (mc.level != null && TFConfig.getClientCloudBlockPrecipitationDistance() > 0) { // Semi vanilla copy of the weather tick, but made to work with cloud blocks instead
				Vec3 vec3 = mc.gameRenderer.getMainCamera().position();
				if (mc.level.getGameTime() % 10L == 0L) {
					RENDER_HELPER.clear();

					double camX = vec3.x();
					double camY = vec3.y();
					double camZ = vec3.z();

					int floorX = Mth.floor(camX);
					int floorY = Mth.floor(camY);
					int floorZ = Mth.floor(camZ);

					int renderDistance = mc.options.weatherRadius().get();
					int precipitationDistance = TFConfig.getClientCloudBlockPrecipitationDistance();
					BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

					for (int roofZ = floorZ - renderDistance; roofZ <= floorZ + renderDistance; ++roofZ) {
						for (int roofX = floorX - renderDistance; roofX <= floorX + renderDistance; ++roofX) {
							int lastBadYLevel = Integer.MIN_VALUE;
							for (int roofY = floorY - renderDistance; roofY < floorY + precipitationDistance + renderDistance; roofY++) {
								boolean skipLoop = roofY == lastBadYLevel + 1; // Cloud can't rain if there is an invalid blockState right below it, so might as well skip the loop
								pos.set(roofX, roofY, roofZ);
								if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(pos)))
									lastBadYLevel = roofY; // Check if we skip next loop
								if (skipLoop) continue;

								if (mc.level.getBlockState(pos).getBlock() instanceof CloudBlock cloudBlock) {
									Pair<Biome.Precipitation, Float> precipitationRainLevelPair = cloudBlock.getCurrentPrecipitation(pos, mc.level, mc.level.getRainLevel(1.0F));
									if (precipitationRainLevelPair.getLeft() == Biome.Precipitation.NONE)
										continue; // No rain no gain

									int highestRainyBlock = roofY;
									for (int y = roofY - 1; y > roofY - precipitationDistance; y--) {
										if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(pos.atY(y))))
											highestRainyBlock = y;
										else break;
									}
									if (highestRainyBlock == roofY) continue;

									RENDER_HELPER.add(new PrecipitationRenderHelper(pos.immutable(), precipitationRainLevelPair.getLeft(), precipitationRainLevelPair.getRight(), highestRainyBlock));
								}
							}
						}
					}
				}

				if (!RENDER_HELPER.isEmpty()) {
					RandomSource randomsource = RandomSource.create(mc.level.getGameTime() * 312987231L);
					BlockPos particlePos = null;
					int weatherRadius = mc.options.weatherRadius().get();
					int weatherDiameter = 2 * weatherRadius + 1;
					int weatherArea = weatherDiameter * weatherDiameter;
					int particleCount = (int) (0.225F * weatherArea) / (mc.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

					boolean yetToMakeASound = true;
					BlockPos camPos = BlockPos.containing(vec3);

					List<Vec2i> particleChecks = new ArrayList<>();
					for (int i = 0; i < particleCount; ++i) {
						particleChecks.add(new Vec2i(randomsource.nextInt(weatherDiameter) - weatherRadius + camPos.getX(), randomsource.nextInt(weatherDiameter) - weatherRadius + camPos.getZ()));
					}

					for (PrecipitationRenderHelper helper : RENDER_HELPER) {
						if (helper.precipitation() == Biome.Precipitation.RAIN) {
							for (Vec2i vec2 : particleChecks) {
								if (vec2.x == helper.cloudPos().getX() && vec2.z == helper.cloudPos().getZ()) {
									BlockPos highestRainyPos = helper.cloudPos().atY(helper.rainOnY());
									if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(highestRainyPos.below())))
										continue;

									if (yetToMakeASound && particlePos != null && randomsource.nextInt(3) < TFWeatherRenderer.rainSoundTime++) {
										TFWeatherRenderer.rainSoundTime = 0;
										if (particlePos.getY() > camPos.getY() + 1 && mc.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, camPos).getY() > Mth.floor((float) camPos.getY())) {
											mc.level.playLocalSound(particlePos, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
										} else {
											mc.level.playLocalSound(particlePos, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
										}
										yetToMakeASound = false;
									}

									if (highestRainyPos.getY() > mc.level.getMinY() && highestRainyPos.getY() <= camPos.getY() + 10 && highestRainyPos.getY() >= camPos.getY() - 10) {
										particlePos = highestRainyPos.below();
										if (mc.options.particles().get() == ParticleStatus.MINIMAL) break;

										double particleX = randomsource.nextDouble();
										double particleZ = randomsource.nextDouble();
										BlockState blockstate = mc.level.getBlockState(particlePos);
										FluidState fluidstate = mc.level.getFluidState(particlePos);
										VoxelShape voxelshape = blockstate.getCollisionShape(mc.level, particlePos);
										double voxelMax = voxelshape.max(Direction.Axis.Y, particleX, particleZ);
										double fluidMax = fluidstate.getHeight(mc.level, particlePos);
										double particleY = Math.max(voxelMax, fluidMax);
										ParticleOptions particleoptions = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
										mc.level.addParticle(particleoptions, (double) particlePos.getX() + particleX, (double) particlePos.getY() + particleY, (double) particlePos.getZ() + particleZ, 0.0D, 0.0D, 0.0D);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	protected static void renderPrecipitation(RenderLevelStageEvent.AfterWeather event) {
		BATCH.render(event.getRenderPass(), RenderPipelines.WEATHER);
	}

	protected static void renderPrecipitationOit(RenderLevelStageEvent.OitTranslucent event) {
		BATCH.render(event.getRenderPass(), RenderPipelines.OIT_WEATHER.getPipeline(event.getOitStage()));
	}

	protected static void preparePrecipitation(PrepareRenderBuffersEvent event) {
		BATCH.clear();
		if (TFConfig.getClientCloudBlockPrecipitationDistance() > 0 && !RENDER_HELPER.isEmpty()) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.level == null) {
				BATCH.upload();
				return;
			}
			float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
			int ticks = (int) (minecraft.level.getGameTime() & 131071L);

			Vec3 vec3 = event.getLevelRenderState().cameraRenderState.pos;
			double camX = vec3.x();
			double camY = vec3.y();
			double camZ = vec3.z();

			int floorY = Mth.floor(camY);

			int renderDistance = minecraft.options.weatherRadius().get();

			float fullTick = (float) ticks + partialTick;
			BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

			for (PrecipitationRenderHelper helper : RENDER_HELPER) {
				BlockPos pos = helper.cloudPos();
				int roofX = pos.getX();
				int roofZ = pos.getZ();

				int botY = Math.max(helper.rainOnY(), floorY - renderDistance);
				int topY = Math.min(pos.getY(), floorY + renderDistance);
				if (topY - botY <= 0) continue;

				double xDiff = (double) roofX + 0.5D - camX;
				double zDiff = (double) roofZ + 0.5D - camZ;
				double columnDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
				double rainX = columnDistance == 0.0D ? 0.0D : -zDiff / columnDistance * 0.5D;
				double rainZ = columnDistance == 0.0D ? 0.0D : xDiff / columnDistance * 0.5D;

				RandomSource random = RandomSource.create((long) roofX * roofX * 3121 + roofX * 45238971L ^ (long) roofZ * roofZ * 418711 + roofZ * 13761L);
				if (helper.precipitation() == Biome.Precipitation.RAIN) {

					int offset = ticks + roofX * roofX * 3121 + roofX * 45238971 + roofZ * roofZ * 418711 + roofZ * 13761 & 31;
					float uvOffset = -((float) offset + partialTick) / 32.0F * (3.0F + random.nextFloat());
					float distance = (float) (columnDistance / (double) renderDistance);
					float alpha = ((1.0F - distance * distance) * 0.5F + 0.5F) * helper.precipitationLevel();
					mutableBlockPos.set(roofX, Math.max(helper.rainOnY(), floorY), roofZ);
					int lightCoords = LightCoordsUtil.getLightCoords(minecraft.level, mutableBlockPos);

					BATCH.addVertex(TFWeatherRenderer.RAIN_TEXTURES, (float) (roofX - camX - rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ - rainZ + 0.5D), 0.0F, (float) botY * 0.25F + uvOffset, 1.0F, 1.0F, 1.0F, alpha, lightCoords);
					BATCH.addVertex(TFWeatherRenderer.RAIN_TEXTURES, (float) (roofX - camX + rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ + rainZ + 0.5D), 1.0F, (float) botY * 0.25F + uvOffset, 1.0F, 1.0F, 1.0F, alpha, lightCoords);
					BATCH.addVertex(TFWeatherRenderer.RAIN_TEXTURES, (float) (roofX - camX + rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ + rainZ + 0.5D), 1.0F, (float) topY * 0.25F + uvOffset, 1.0F, 1.0F, 1.0F, alpha, lightCoords);
					BATCH.addVertex(TFWeatherRenderer.RAIN_TEXTURES, (float) (roofX - camX - rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ - rainZ + 0.5D), 0.0F, (float) topY * 0.25F + uvOffset, 1.0F, 1.0F, 1.0F, alpha, lightCoords);
				} else if (helper.precipitation() == Biome.Precipitation.SNOW) {

					float offset = -((float) (ticks & 511) + partialTick) / 512.0F;
					float uOffset = (float) (random.nextDouble() + (double) fullTick * 0.01D * (double) ((float) random.nextGaussian()));
					float vOffset = (float) (random.nextDouble() + (double) (fullTick * (float) random.nextGaussian()) * 0.001D);
					float distance = (float) (columnDistance / (double) renderDistance);
					float alpha = ((1.0F - distance * distance) * 0.3F + 0.5F) * helper.precipitationLevel();
					mutableBlockPos.set(roofX, Math.max(helper.rainOnY(), floorY), roofZ);

					int lightCoords = LightCoordsUtil.getLightCoords(minecraft.level, mutableBlockPos);
					int u = lightCoords & '\uffff';
					int v = lightCoords >> 16 & '\uffff';
					u = (u * 3 + 240) / 4;
					v = (v * 3 + 240) / 4;

					int snowLight = u | v << 16;

					BATCH.addVertex(TFWeatherRenderer.SNOW_TEXTURES, (float) (roofX - camX - rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ - rainZ + 0.5D), 0.0F + uOffset, (float) botY * 0.25F + offset + vOffset, 1.0F, 1.0F, 1.0F, alpha, snowLight);
					BATCH.addVertex(TFWeatherRenderer.SNOW_TEXTURES, (float) (roofX - camX + rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ + rainZ + 0.5D), 1.0F + uOffset, (float) botY * 0.25F + offset + vOffset, 1.0F, 1.0F, 1.0F, alpha, snowLight);
					BATCH.addVertex(TFWeatherRenderer.SNOW_TEXTURES, (float) (roofX - camX + rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ + rainZ + 0.5D), 1.0F + uOffset, (float) topY * 0.25F + offset + vOffset, 1.0F, 1.0F, 1.0F, alpha, snowLight);
					BATCH.addVertex(TFWeatherRenderer.SNOW_TEXTURES, (float) (roofX - camX - rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ - rainZ + 0.5D), 0.0F + uOffset, (float) topY * 0.25F + offset + vOffset, 1.0F, 1.0F, 1.0F, alpha, snowLight);
				}
			}
		}
		BATCH.upload();
	}
}
