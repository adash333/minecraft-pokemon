package com.adash333.minecraftpokemon.client.renderer;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.world.AllayTransformation;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftPokemonMod.MOD_ID, value = Dist.CLIENT)
public final class AllayPlayerRenderer {
    // Render-only stand-ins: never spawn AI entities or transfer control away from the player.
    private static final Map<AbstractClientPlayer, Allay> MODELS = new WeakHashMap<>();

    @SubscribeEvent
    public static void render(RenderPlayerEvent.Pre event) {
        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
        if (!AllayTransformation.isAllay(player)) {
            MODELS.remove(player);
            return;
        }
        event.setCanceled(true);
        if (player.isSpectator() || player.isInvisible()) return;
        Allay allay = MODELS.computeIfAbsent(player, key -> new Allay(EntityType.ALLAY, key.level()));
        allay.tickCount = player.tickCount;
        allay.setPos(player.position());
        allay.setYRot(player.getYRot());
        allay.yRotO = player.yRotO;
        allay.yBodyRot = player.yBodyRot;
        allay.yBodyRotO = player.yBodyRotO;
        allay.yHeadRot = player.yHeadRot;
        allay.yHeadRotO = player.yHeadRotO;
        allay.setXRot(player.getXRot());
        allay.xRotO = player.xRotO;
        allay.setDeltaMovement(player.getDeltaMovement());
        allay.setItemSlot(EquipmentSlot.MAINHAND, player.getMainHandItem());
        allay.setItemSlot(EquipmentSlot.OFFHAND, player.getOffhandItem());
        allay.setLeftHanded(player.getMainArm() == net.minecraft.world.entity.HumanoidArm.LEFT);
        allay.hurtTime = player.hurtTime;
        allay.deathTime = player.deathTime;
        event.getPoseStack().pushPose();
        Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(allay).render(allay,
                player.getYRot(), event.getPartialTick(), event.getPoseStack(),
                event.getMultiBufferSource(), event.getPackedLight());
        event.getPoseStack().popPose();
    }

    private AllayPlayerRenderer() { }
}
