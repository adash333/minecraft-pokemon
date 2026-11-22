package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.entity.UnicornEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;

public final class UnicornRenderer extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(MinecraftPokemonMod.MOD_ID, "unicorn"), "main");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/horse/horse_white.png");

    private static final ResourceLocation COLORS =
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "textures/entity/unicorn_colors.png");

    public UnicornRenderer(EntityRendererProvider.Context context) {
        super(context, createModel(context, false), 1.1F);
        addLayer(new ColorLayer(createModel(context, true)));
        addLayer(new HorseArmorLayer(this, context.getModelSet()));
    }

    private static HorseModel<Horse> createModel(EntityRendererProvider.Context context, boolean colors) {
        ModelPart root = context.bakeLayer(LAYER_LOCATION);
        if (colors) {
            // Keep the animated parent transforms and draw only the three colored parts.
            root.getAllParts().forEach(part -> part.skipDraw = true);
        }
        ModelPart head = root.getChild("head_parts");
        head.getChild("mane").skipDraw = !colors;
        head.getChild("horn").skipDraw = !colors;
        root.getChild("body").getChild("tail").skipDraw = !colors;
        return new HorseModel<>(root);
    }

    @Override
    public ResourceLocation getTextureLocation(Horse horse) {
        return TEXTURE;
    }

    @Override
    protected void setupRotations(Horse horse, PoseStack pose, float ageInTicks, float yaw, float partialTick) {
        super.setupRotations(horse, pose, ageInTicks, yaw, partialTick);
        if (horse instanceof UnicornEntity unicorn) {
            float roll = unicorn.getRollProgress(partialTick);
            if (roll > 0.0F) {
                float pivot = horse.getBbHeight() * 0.45F;
                pose.translate(0.0F, pivot, 0.0F);
                pose.mulPose(Axis.ZP.rotationDegrees(360.0F * roll));
                pose.translate(0.0F, -pivot, 0.0F);
            }
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HorseModel.createBodyMesh(CubeDeformation.NONE);
        mesh.getRoot().getChild("head_parts").addOrReplaceChild("horn",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0F, -16.0F, -1.5F, 2.0F, 5.0F, 2.0F)
                        .texOffs(8, 0).addBox(-0.5F, -18.0F, -1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    private final class ColorLayer extends RenderLayer<Horse, HorseModel<Horse>> {
        private final HorseModel<Horse> colors;

        private ColorLayer(HorseModel<Horse> colors) {
            super(UnicornRenderer.this);
            this.colors = colors;
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource buffers, int light, Horse horse,
                           float limbSwing, float limbSwingAmount, float partialTick,
                           float ageInTicks, float headYaw, float headPitch) {
            coloredCutoutModelCopyLayerRender(getParentModel(), colors, COLORS, pose, buffers, light, horse,
                    limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, partialTick, 1.0F, 1.0F, 1.0F);
        }
    }
}
