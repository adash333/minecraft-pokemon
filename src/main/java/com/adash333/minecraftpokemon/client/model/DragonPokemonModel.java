package com.adash333.minecraftpokemon.client.model;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.entity.DragonPokemonEntity;
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

public class DragonPokemonModel extends EntityModel<DragonPokemonEntity> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(MinecraftPokemonMod.MOD_ID, "dragon_pokemon"), "main");

    private final ModelPart root;
    private final ModelPart miniryu;
    private final ModelPart miniryuHead;
    private final ModelPart miniryuTail;
    private final ModelPart hakuryu;
    private final ModelPart hakuryuHead;
    private final ModelPart hakuryuTail;
    private final ModelPart kairyu;
    private final ModelPart kairyuHead;
    private final ModelPart kairyuTail;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public DragonPokemonModel(ModelPart root) {
        this.root = root;
        this.miniryu = root.getChild("miniryu");
        this.miniryuHead = miniryu.getChild("head");
        this.miniryuTail = miniryu.getChild("body").getChild("tail");
        this.hakuryu = root.getChild("hakuryu");
        this.hakuryuHead = hakuryu.getChild("head");
        this.hakuryuTail = hakuryu.getChild("body").getChild("tail");
        this.kairyu = root.getChild("kairyu");
        this.kairyuHead = kairyu.getChild("head");
        this.kairyuTail = kairyu.getChild("body").getChild("tail");
        this.leftWing = kairyu.getChild("body").getChild("left_wing");
        this.rightWing = kairyu.getChild("body").getChild("right_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition miniryu = root.addOrReplaceChild("miniryu", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition miniryuHead = miniryu.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -4.0F, 8.0F, 7.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 10.5F, -2.0F));
        miniryuHead.addOrReplaceChild("horn",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.5F, -0.5F, -0.25F, 0.0F, 0.0F));
        miniryuHead.addOrReplaceChild("left_fin",
                CubeListBuilder.create().texOffs(56, 0)
                        .addBox(0.0F, -3.0F, -1.0F, 1.0F, 6.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(3.8F, -0.5F, 0.0F, 0.0F, -0.25F, -0.45F));
        miniryuHead.addOrReplaceChild("right_fin",
                CubeListBuilder.create().texOffs(68, 0)
                        .addBox(-1.0F, -3.0F, -1.0F, 1.0F, 6.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-3.8F, -0.5F, 0.0F, 0.0F, 0.25F, 0.45F));

        PartDefinition miniryuBody = miniryu.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-3.0F, -4.0F, -3.0F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 18.0F, 0.0F));
        miniryuBody.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(28, 20)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 15.0F, CubeDeformation.NONE)
                        .texOffs(28, 40)
                        .addBox(-1.5F, -1.5F, 14.0F, 3.0F, 3.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 2.0F, 2.0F, 0.15F, 0.25F, 0.0F));

        PartDefinition hakuryu = root.addOrReplaceChild("hakuryu", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition hakuryuHead = hakuryu.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.5F, -4.0F, -4.5F, 9.0F, 7.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 6.5F, -5.0F));
        hakuryuHead.addOrReplaceChild("horn",
                CubeListBuilder.create().texOffs(48, 0)
                        .addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -3.5F, -0.5F, -0.2F, 0.0F, 0.0F));
        hakuryuHead.addOrReplaceChild("left_wing_ear",
                CubeListBuilder.create().texOffs(56, 0)
                        .addBox(0.0F, -5.0F, -1.0F, 1.0F, 10.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.0F, -1.5F, 0.0F, 0.0F, -0.2F, -0.45F));
        hakuryuHead.addOrReplaceChild("right_wing_ear",
                CubeListBuilder.create().texOffs(72, 0)
                        .addBox(-1.0F, -5.0F, -1.0F, 1.0F, 10.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, -1.5F, 0.0F, 0.0F, 0.2F, 0.45F));
        hakuryuHead.addOrReplaceChild("neck_pearl",
                CubeListBuilder.create().texOffs(92, 0)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 5.0F, -1.0F));

        PartDefinition hakuryuBody = hakuryu.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 12.0F, CubeDeformation.NONE)
                        .texOffs(44, 24)
                        .addBox(-3.0F, -13.0F, -5.0F, 6.0F, 11.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 18.0F, 1.0F));
        PartDefinition hakuryuTail = hakuryuBody.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 48)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 18.0F, CubeDeformation.NONE)
                        .texOffs(0, 74)
                        .addBox(-2.0F, -2.0F, 17.0F, 4.0F, 4.0F, 14.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.05F, 0.3F, 0.0F));
        hakuryuTail.addOrReplaceChild("tail_pearl",
                CubeListBuilder.create().texOffs(50, 50)
                        .addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 29.0F));

        PartDefinition kairyu = root.addOrReplaceChild("kairyu", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition kairyuHead = kairyu.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.5F, -4.5F, -5.0F, 11.0F, 9.0F, 9.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 6.0F, -2.5F));
        kairyuHead.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(42, 0)
                        .addBox(-3.0F, -1.0F, -3.5F, 6.0F, 3.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 1.2F, -4.0F));
        kairyuHead.addOrReplaceChild("left_eye",
                CubeListBuilder.create().texOffs(100, 105)
                        .addBox(0.8F, -2.6F, -5.7F, 3.2F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.ZERO);
        kairyuHead.addOrReplaceChild("right_eye",
                CubeListBuilder.create().texOffs(113, 105)
                        .addBox(-4.0F, -2.6F, -5.7F, 3.2F, 4.0F, 1.0F, CubeDeformation.NONE),
                PartPose.ZERO);
        kairyuHead.addOrReplaceChild("left_antenna",
                CubeListBuilder.create().texOffs(68, 0)
                        .addBox(0.0F, -8.0F, -1.0F, 1.0F, 9.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.5F, -3.0F, -1.0F, -0.35F, 0.0F, 0.25F));
        kairyuHead.addOrReplaceChild("right_antenna",
                CubeListBuilder.create().texOffs(74, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 1.0F, 9.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-2.5F, -3.0F, -1.0F, -0.35F, 0.0F, -0.25F));

        PartDefinition kairyuBody = kairyu.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-5.0F, -7.0F, -3.5F, 10.0F, 13.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        kairyuBody.addOrReplaceChild("belly",
                CubeListBuilder.create().texOffs(42, 24)
                        .addBox(-3.5F, -6.0F, -1.0F, 7.0F, 11.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, -3.2F));
        kairyuBody.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(64, 24)
                        .addBox(0.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.5F, -4.0F, -0.5F, -0.2F, 0.0F, -0.35F));
        kairyuBody.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(82, 24)
                        .addBox(-4.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.5F, -4.0F, -0.5F, -0.2F, 0.0F, 0.35F));
        kairyuBody.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(64, 44)
                        .addBox(-2.0F, 0.0F, -3.0F, 5.0F, 8.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offset(3.0F, 5.0F, 0.0F));
        kairyuBody.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(90, 44)
                        .addBox(-3.0F, 0.0F, -3.0F, 5.0F, 8.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offset(-3.0F, 5.0F, 0.0F));
        kairyuBody.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(0, 52)
                        .addBox(0.0F, -8.0F, 0.0F, 2.0F, 15.0F, 12.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.0F, -5.0F, 2.0F, 0.15F, 0.25F, -0.45F));
        kairyuBody.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(32, 52)
                        .addBox(-2.0F, -8.0F, 0.0F, 2.0F, 15.0F, 12.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, -5.0F, 2.0F, 0.15F, -0.25F, 0.45F));
        kairyuBody.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(64, 64)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 18.0F, CubeDeformation.NONE)
                        .texOffs(64, 90)
                        .addBox(-2.0F, -2.0F, 17.0F, 4.0F, 4.0F, 12.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 2.0F, 3.0F, -0.15F, 0.25F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(DragonPokemonEntity entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch) {
        int stage = entity.getStage();
        miniryu.visible = stage == DragonPokemonEntity.MINIRYU_STAGE;
        hakuryu.visible = stage == DragonPokemonEntity.HAKURYU_STAGE;
        kairyu.visible = stage == DragonPokemonEntity.KAIRYU_STAGE;

        float yaw = netHeadYaw * ((float) Math.PI / 180.0F);
        float pitch = headPitch * ((float) Math.PI / 180.0F);
        miniryuHead.yRot = yaw;
        miniryuHead.xRot = pitch;
        hakuryuHead.yRot = yaw;
        hakuryuHead.xRot = pitch;
        kairyuHead.yRot = yaw;
        kairyuHead.xRot = pitch;

        miniryuTail.yRot = 0.25F + Mth.sin(ageInTicks * 0.14F) * 0.28F;
        hakuryuTail.yRot = 0.3F + Mth.sin(ageInTicks * 0.1F) * 0.35F;
        kairyuTail.yRot = 0.25F + Mth.sin(ageInTicks * 0.09F) * 0.25F;
        leftWing.zRot = -0.45F - Mth.sin(ageInTicks * 0.2F) * 0.25F;
        rightWing.zRot = 0.45F + Mth.sin(ageInTicks * 0.2F) * 0.25F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
            int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
