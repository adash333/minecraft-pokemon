package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.client.event.RenderArmEvent;

/** Renders the bundled character skin only on the local player. */
public final class PlayerCharacterSkinLayer
        extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation CLASSIC_TEXTURE = new ResourceLocation(
            MinecraftPokemonMod.MOD_ID, "textures/entity/player/twin_tail_girl_classic.png");
    private static final ResourceLocation SLIM_TEXTURE = new ResourceLocation(
            MinecraftPokemonMod.MOD_ID, "textures/entity/player/twin_tail_girl_slim.png");

    private final ResourceLocation texture;

    private PlayerCharacterSkinLayer(PlayerRenderer renderer, ResourceLocation texture) {
        super(renderer);
        this.texture = texture;
    }

    public static PlayerCharacterSkinLayer classic(PlayerRenderer renderer) {
        return new PlayerCharacterSkinLayer(renderer, CLASSIC_TEXTURE);
    }

    public static PlayerCharacterSkinLayer slim(PlayerRenderer renderer) {
        return new PlayerCharacterSkinLayer(renderer, SLIM_TEXTURE);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player != Minecraft.getInstance().player || player.isInvisible()) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = getParentModel();
        OuterVisibility previousVisibility = OuterVisibility.capture(model);
        previousVisibility.showAll(model);

        poseStack.pushPose();
        poseStack.scale(1.001F, 1.001F, 1.001F);
        VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, vertices, packedLight,
                LivingEntityRenderer.getOverlayCoords(player, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        previousVisibility.restore(model);
    }

    public static void renderFirstPersonArm(RenderArmEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        AbstractClientPlayer player = event.getPlayer();
        if (player != minecraft.player || player.isInvisible()) {
            return;
        }

        EntityRenderer<?> entityRenderer = minecraft.getEntityRenderDispatcher().getRenderer(player);
        if (!(entityRenderer instanceof PlayerRenderer playerRenderer)) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
        boolean right = event.getArm() == HumanoidArm.RIGHT;
        ModelPart arm = right ? model.rightArm : model.leftArm;
        ModelPart sleeve = right ? model.rightSleeve : model.leftSleeve;
        boolean armVisible = arm.visible;
        boolean sleeveVisible = sleeve.visible;

        model.attackTime = 0.0F;
        model.crouching = false;
        model.swimAmount = 0.0F;
        model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        arm.xRot = 0.0F;
        sleeve.xRot = 0.0F;
        arm.visible = true;
        sleeve.visible = true;

        ResourceLocation texture = "slim".equals(player.getModelName()) ? SLIM_TEXTURE : CLASSIC_TEXTURE;
        VertexConsumer solid = event.getMultiBufferSource().getBuffer(RenderType.entitySolid(texture));
        arm.render(event.getPoseStack(), solid, event.getPackedLight(), 0);
        VertexConsumer translucent = event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(texture));
        sleeve.render(event.getPoseStack(), translucent, event.getPackedLight(), 0);

        arm.visible = armVisible;
        sleeve.visible = sleeveVisible;
        event.setCanceled(true);
    }

    private record OuterVisibility(boolean hat, boolean jacket, boolean leftSleeve,
            boolean rightSleeve, boolean leftPants, boolean rightPants) {
        private static OuterVisibility capture(PlayerModel<AbstractClientPlayer> model) {
            return new OuterVisibility(model.hat.visible, model.jacket.visible,
                    model.leftSleeve.visible, model.rightSleeve.visible,
                    model.leftPants.visible, model.rightPants.visible);
        }

        private void showAll(PlayerModel<AbstractClientPlayer> model) {
            model.hat.visible = true;
            model.jacket.visible = true;
            model.leftSleeve.visible = true;
            model.rightSleeve.visible = true;
            model.leftPants.visible = true;
            model.rightPants.visible = true;
        }

        private void restore(PlayerModel<AbstractClientPlayer> model) {
            model.hat.visible = hat;
            model.jacket.visible = jacket;
            model.leftSleeve.visible = leftSleeve;
            model.rightSleeve.visible = rightSleeve;
            model.leftPants.visible = leftPants;
            model.rightPants.visible = rightPants;
        }
    }
}
