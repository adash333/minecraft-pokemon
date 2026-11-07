package com.adash333.minecraftpokemon.item;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.entity.WeaponProjectile;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class FirearmItem extends Item {
    private static final double LOCK_RANGE = 48.0;
    private static final String PROTOCOL = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "firearms"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
    private static final Map<ServerPlayer, Lock> LOCKS = new WeakHashMap<>();

    public FirearmItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public static void registerNetwork() {
        CHANNEL.messageBuilder(FirearmInput.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder((input, buffer) -> buffer.writeBoolean(input.lockOn()))
                .decoder(buffer -> new FirearmInput(buffer.readBoolean()))
                .consumerMainThread((input, context) -> {
                    ServerPlayer player = context.get().getSender();
                    if (player != null && player.isAlive() && !player.isSpectator()
                            && player.getMainHandItem().getItem() instanceof FirearmItem firearm) {
                        if (input.lockOn()) {
                            updateLock(player);
                        } else {
                            firearm.fire(player);
                        }
                    }
                })
                .add();
    }

    public static void sendInput(boolean lockOn) {
        CHANNEL.sendToServer(new FirearmInput(lockOn));
    }

    private static void updateLock(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(LOCK_RANGE));
        Vec3 clearEnd = level.clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, eye, clearEnd,
                player.getBoundingBox().expandTowards(clearEnd.subtract(eye)).inflate(1.0),
                entity -> entity.isPickable() && WeaponProjectile.canTarget(player, entity),
                eye.distanceToSqr(clearEnd));
        if (hit == null) {
            LOCKS.remove(player);
            player.displayClientMessage(Component.translatable("message.minecraftpokemon.firearm.no_target"), true);
            return;
        }
        Entity hitEntity = hit.getEntity();
        Entity target = hitEntity instanceof PartEntity<?> part ? part.getParent() : hitEntity;
        LOCKS.put(player, new Lock(player.getMainHandItem(), level, target.getUUID(), player.tickCount + 200));
        player.displayClientMessage(Component.translatable("message.minecraftpokemon.firearm.locked",
                target.getDisplayName()), true);
    }

    private void fire(ServerPlayer player) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return;
        }
        ServerLevel level = player.serverLevel();
        WeaponProjectile bullet = WeaponProjectile.bullet(player, getLockedTarget(player, level));
        if (level.addFreshEntity(bullet)) {
            player.getCooldowns().addCooldown(this, 8);
            player.swing(InteractionHand.MAIN_HAND, true);
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST,
                    SoundSource.PLAYERS, 0.75F, 1.35F);
        }
    }

    @Nullable
    private static Entity getLockedTarget(ServerPlayer player, ServerLevel level) {
        Lock lock = LOCKS.get(player);
        if (lock == null) {
            return null;
        }
        if (lock.level() == level && lock.stack() == player.getMainHandItem() && player.tickCount < lock.expires()) {
            Entity target = level.getEntity(lock.target());
            if (target != null && target.distanceToSqr(player) <= LOCK_RANGE * LOCK_RANGE
                    && WeaponProjectile.canTarget(player, target)) {
                return target;
            }
        }
        LOCKS.remove(player);
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.minecraftpokemon.firearm.controls"));
    }

    private record Lock(ItemStack stack, ServerLevel level, UUID target, int expires) {}
    private record FirearmInput(boolean lockOn) {}
}
