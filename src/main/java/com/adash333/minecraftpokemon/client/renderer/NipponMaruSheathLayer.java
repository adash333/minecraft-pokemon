package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.item.NipponMaruItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;

public final class NipponMaruSheathLayer
        extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public static final ResourceLocation MODEL = new ResourceLocation(
            MinecraftPokemonMod.MOD_ID, "item/nippon_maru_sheath");

    public NipponMaruSheathLayer(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int light, AbstractClientPlayer player,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
            float netHeadYaw, float headPitch) {
        InteractionHand hand = NipponMaruItem.heldHand(player);
        if (hand == null || player.isInvisible() || player.isSpectator()) {
            return;
        }

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        pose.pushPose();
        getParentModel().body.translateAndRotate(pose);
        pose.translate(0.36, 1.01, 0.10);
        pose.mulPose(Axis.ZP.rotationDegrees(-13.0F));
        pose.mulPose(Axis.XP.rotationDegrees(23.0F));
        renderer.render(player.getItemInHand(hand), ItemDisplayContext.NONE, false, pose, buffer,
                light, OverlayTexture.NO_OVERLAY, renderer.getItemModelShaper().getModelManager().getModel(MODEL));
        pose.popPose();
    }
}
