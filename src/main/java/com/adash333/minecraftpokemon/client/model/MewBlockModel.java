package com.adash333.minecraftpokemon.client.model;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.entity.MewBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MewBlockModel extends EntityModel<MewBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(MinecraftPokemonMod.MOD_ID, "mew_block"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart tail;

    public MewBlockModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.leftEar = head.getChild("left_ear");
        this.rightEar = head.getChild("right_ear");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -5.0F, -2.0F, 6.0F, 8.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 6.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 11.0F, 0.0F));

        head.addOrReplaceChild("left_ear",
                CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, -6.0F, -1.0F, 2.0F, 7.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.7F, -2.5F, -0.5F, 0.0F, 0.0F, -0.55F));
        head.addOrReplaceChild("right_ear",
                CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -6.0F, -1.0F, 2.0F, 7.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-2.7F, -2.5F, -0.5F, 0.0F, 0.0F, 0.55F));

        body.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(24, 18).addBox(0.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(3.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.65F));
        body.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-3.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.65F));
        body.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(24, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(1.7F, 2.5F, 0.0F));
        body.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(32, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(-1.7F, 2.5F, 0.0F));

        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 18.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 16.0F, 2.0F, -0.15F, 0.35F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(MewBlockEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        head.xRot = headPitch * ((float) Math.PI / 180.0F);
        leftEar.zRot = -0.55F + Mth.sin(ageInTicks * 0.08F) * 0.08F;
        rightEar.zRot = 0.55F - Mth.sin(ageInTicks * 0.08F) * 0.08F;
        tail.yRot = 0.35F + Mth.sin(ageInTicks * 0.12F) * 0.25F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
