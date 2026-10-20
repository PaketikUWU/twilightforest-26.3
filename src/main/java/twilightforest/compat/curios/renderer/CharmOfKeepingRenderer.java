package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CharmOfKeepingRenderer implements ICurioRenderer {

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext slotContext, PoseStack ms, SubmitNodeCollector buffer, int light, S renderState, RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float yRotation, float xRotation) {
		if (renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
			ms.pushPose();
			model.rightLeg.translateAndRotate(ms);
			ms.translate(-0.0D, 0.15D, -0.15D);
			ms.rotateDegrees(Axis.YP, 0.0F);
			ms.scale(0.3F, -0.3F, -0.3F);
			ItemStackRenderState itemState = new ItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForLiving(itemState, stack, ItemDisplayContext.FIXED, slotContext.entity());
			itemState.submit(ms, buffer, light, OverlayTexture.NO_OVERLAY, 0);
			ms.popPose();
		}
	}
}
