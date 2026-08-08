package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.client.model.MewBlockModel;
import com.adash333.minecraftpokemon.entity.MewBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MewBlockRenderer extends MobRenderer<MewBlockEntity, MewBlockModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "textures/entity/mew_block.png");

    public MewBlockRenderer(EntityRendererProvider.Context context) {
        super(context, new MewBlockModel(context.bakeLayer(MewBlockModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public void render(MewBlockEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        if (entity.isEvolved()) {
            poseStack.scale(1.25F, 1.25F, 1.25F);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MewBlockEntity entity) {
        return TEXTURE;
    }
}
