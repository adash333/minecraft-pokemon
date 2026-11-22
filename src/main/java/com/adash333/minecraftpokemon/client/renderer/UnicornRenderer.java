package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;

public final class UnicornRenderer extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(MinecraftPokemonMod.MOD_ID, "unicorn"), "main");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/horse/horse_white.png");

    public UnicornRenderer(EntityRendererProvider.Context context) {
        super(context, new HorseModel<>(context.bakeLayer(LAYER_LOCATION)), 1.1F);
        addLayer(new HorseArmorLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Horse horse) {
        return TEXTURE;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HorseModel.createBodyMesh(CubeDeformation.NONE);
        mesh.getRoot().getChild("head_parts").addOrReplaceChild("horn",
                CubeListBuilder.create()
                        .texOffs(7, 18).addBox(-1.0F, -16.0F, -1.5F, 2.0F, 5.0F, 2.0F)
                        .texOffs(7, 19).addBox(-0.5F, -18.0F, -1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }
}
