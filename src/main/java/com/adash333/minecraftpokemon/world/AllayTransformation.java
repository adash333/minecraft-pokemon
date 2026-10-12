package com.adash333.minecraftpokemon.world;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.registry.ModEffects;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftPokemonMod.MOD_ID)
public final class AllayTransformation {
    private static final String ACTIVE = "minecraftpokemon_allay_active";
    private static final String GRANTED_FLIGHT = "minecraftpokemon_allay_granted_flight";

    public static boolean isAllay(Player player) {
        return ModEffects.ALLAY_FORM.isPresent() && player.hasEffect(ModEffects.ALLAY_FORM.get());
    }

    @SubscribeEvent
    public static void tick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        boolean active = isAllay(player);
        var data = player.getPersistentData();
        boolean changed = data.getBoolean(ACTIVE) != active;
        if (changed) {
            data.putBoolean(ACTIVE, active);
            player.refreshDimensions();
        }
        // Also refresh after login/respawn, when persisted state and dimensions may differ.
        if (active && Math.abs(player.getBbHeight() - 0.6F) > 0.001F) player.refreshDimensions();
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (changed) {
            if (active) {
                serverPlayer.serverLevel().getChunkSource().broadcast(player,
                        new ClientboundUpdateMobEffectPacket(player.getId(), player.getEffect(ModEffects.ALLAY_FORM.get())));
            } else {
                serverPlayer.serverLevel().getChunkSource().broadcast(player,
                        new ClientboundRemoveMobEffectPacket(player.getId(), ModEffects.ALLAY_FORM.get()));
            }
        }
        if (active) {
            player.fallDistance = 0;
            boolean abilitiesChanged = !player.getAbilities().mayfly || !player.getAbilities().flying;
            if (!player.getAbilities().mayfly) {
                data.putBoolean(GRANTED_FLIGHT, true);
            }
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            if (abilitiesChanged) serverPlayer.onUpdateAbilities();
        } else if (data.getBoolean(GRANTED_FLIGHT)) {
            data.remove(GRANTED_FLIGHT);
            if (!player.isCreative() && !player.isSpectator()) {
                boolean wasFlying = player.getAbilities().flying;
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.fallDistance = 0;
                serverPlayer.onUpdateAbilities();
                if (wasFlying) player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
            }
        }
    }

    @SubscribeEvent
    public static void maintainClientFlight(TickEvent.PlayerTickEvent event) {
        // Vanilla can stop local flight after touching the ground or double-jumping.
        // Restore it at both tick boundaries so the next movement stays in flight.
        if (event.player.level().isClientSide && isAllay(event.player)) {
            event.player.getAbilities().mayfly = true;
            event.player.getAbilities().flying = true;
            event.player.fallDistance = 0;
        }
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer observer
                && event.getTarget() instanceof Player target && isAllay(target)) {
            observer.connection.send(new ClientboundUpdateMobEffectPacket(target.getId(),
                    target.getEffect(ModEffects.ALLAY_FORM.get())));
        }
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void size(EntityEvent.Size event) {
        // This hook also runs in Entity's constructor, before effects are initialized.
        if (event.getEntity() instanceof Player player && player.isAddedToWorld() && isAllay(player)) {
            event.setNewSize(EntityDimensions.scalable(0.35F, 0.6F));
            event.setNewEyeHeight(0.36F);
        }
    }

    private AllayTransformation() { }
}
