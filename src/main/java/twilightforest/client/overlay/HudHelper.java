package twilightforest.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Copies of private {@link net.minecraft.client.gui.Hud} helpers that used to be made accessible via access transformers on {@code Gui}.
 */
public class HudHelper {
	private static final Identifier FOOD_EMPTY_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_empty_hunger");
	private static final Identifier FOOD_HALF_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_half_hunger");
	private static final Identifier FOOD_FULL_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_full_hunger");
	private static final Identifier FOOD_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/food_empty");
	private static final Identifier FOOD_HALF_SPRITE = Identifier.withDefaultNamespace("hud/food_half");
	private static final Identifier FOOD_FULL_SPRITE = Identifier.withDefaultNamespace("hud/food_full");
	private static final RandomSource RANDOM = RandomSource.create();

	// [VanillaCopy] Hud.canRenderCrosshairForSpectator
	public static boolean canRenderCrosshairForSpectator(Minecraft minecraft, @Nullable HitResult hitResult) {
		if (hitResult == null) {
			return false;
		} else if (hitResult.getType() == HitResult.Type.ENTITY) {
			return ((EntityHitResult) hitResult).getEntity() instanceof MenuProvider;
		} else if (hitResult.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHitResult = (BlockHitResult) hitResult;
			BlockPos pos = blockHitResult.getBlockPos();
			Level level = minecraft.level;
			BlockState state = level.getBlockState(pos);
			return state.showAsInteractableInSpectatorMode(level, pos, blockHitResult);
		} else {
			return false;
		}
	}

	// [VanillaCopy] Hud.extractFood
	public static void extractFood(Minecraft minecraft, GuiGraphicsExtractor graphics, Player player, int yLineBase, int xRight) {
		FoodData foodData = player.getFoodData();
		int food = foodData.getFoodLevel();

		for (int i = 0; i < 10; i++) {
			int yo = yLineBase;
			Identifier empty;
			Identifier half;
			Identifier full;
			if (player.hasEffect(MobEffects.HUNGER)) {
				empty = FOOD_EMPTY_HUNGER_SPRITE;
				half = FOOD_HALF_HUNGER_SPRITE;
				full = FOOD_FULL_HUNGER_SPRITE;
			} else {
				empty = FOOD_EMPTY_SPRITE;
				half = FOOD_HALF_SPRITE;
				full = FOOD_FULL_SPRITE;
			}

			if (player.getFoodData().getSaturationLevel() <= 0.0F && minecraft.gui.hud.getGuiTicks() % (food * 3 + 1) == 0) {
				yo += RANDOM.nextInt(3) - 1;
			}

			int xo = xRight - i * 8 - 9;
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, xo, yo, 9, 9);
			if (i * 2 + 1 < food) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, xo, yo, 9, 9);
			}

			if (i * 2 + 1 == food) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, half, xo, yo, 9, 9);
			}
		}
	}
}
