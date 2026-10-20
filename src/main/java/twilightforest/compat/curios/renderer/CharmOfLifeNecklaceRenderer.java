package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;

public class CharmOfLifeNecklaceRenderer implements ICurioRenderer {

	private final CharmOfLifeNecklaceModel model;
	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.model = new CharmOfLifeNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE));
		this.necklaceColor = necklaceColor;
	}

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack item, SlotContext slotContext, PoseStack stack, SubmitNodeCollector buffer, int light, S state, RenderLayerParent<S, M> parent, EntityRendererProvider.Context context, float netHeadYaw, float headPitch) {
		if (parent.getModel() instanceof HumanoidModel<?> model) {
			stack.pushPose();
			model.body.translateAndRotate(stack);
			stack.translate(-0.0D, 0.23D, -0.135D);
			stack.rotateDegrees(Axis.YP, 0.0F);
			stack.scale(-0.4F, -0.4F, 0.4F);
			ItemStackRenderState itemState = new ItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForLiving(itemState, item, ItemDisplayContext.FIXED, slotContext.entity());
			itemState.submit(stack, buffer, light, OverlayTexture.NO_OVERLAY, 0);
			stack.popPose();
		}
		ICurioRenderer.setupHumanoidAnimations(this.model, state);
//		VertexConsumer vertexConsumer = buffer.order(1).submit(RenderTypes.entityCutout(TwilightForestMod.getModelTexture("charm_of_life_necklace.png")));
//		this.model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, this.necklaceColor);
	}
}
