package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.client.model.DragonPokemonModel;
import com.adash333.minecraftpokemon.entity.DragonPokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DragonPokemonRenderer extends MobRenderer<DragonPokemonEntity, DragonPokemonModel> {
    private static final ResourceLocation MINIRYU_TEXTURE =
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "textures/entity/miniryu.png");
    private static final ResourceLocation HAKURYU_TEXTURE =
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "textures/entity/hakuryu.png");
    private static final ResourceLocation KAIRYU_TEXTURE =
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "textures/entity/kairyu.png");

    public DragonPokemonRenderer(EntityRendererProvider.Context context) {
        super(context, new DragonPokemonModel(context.bakeLayer(DragonPokemonModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public void render(DragonPokemonEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float scale = switch (entity.getStage()) {
            case DragonPokemonEntity.HAKURYU_STAGE -> 1.05F;
            case DragonPokemonEntity.KAIRYU_STAGE -> 1.2F;
            default -> 0.8F;
        };
        poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(DragonPokemonEntity entity) {
        return switch (entity.getStage()) {
            case DragonPokemonEntity.HAKURYU_STAGE -> HAKURYU_TEXTURE;
            case DragonPokemonEntity.KAIRYU_STAGE -> KAIRYU_TEXTURE;
            default -> MINIRYU_TEXTURE;
        };
    }
}
