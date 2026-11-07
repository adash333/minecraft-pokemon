package com.adash333.minecraftpokemon.item;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.registry.ModItems;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = MinecraftPokemonMod.MOD_ID)
public final class NipponMaruItem extends Item {
    private static final ResourceLocation ID = new ResourceLocation(MinecraftPokemonMod.MOD_ID, "nippon_maru");
    private static final ResourceKey<DamageType> SLASH = ResourceKey.create(Registries.DAMAGE_TYPE, ID);
    private static final String PROTOCOL = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ID, () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public NipponMaruItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    public static void registerNetwork() {
        CHANNEL.messageBuilder(SlashInput.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder((input, buffer) -> buffer.writeBoolean(input.horizontal()))
                .decoder(buffer -> new SlashInput(buffer.readBoolean()))
                .consumerMainThread((input, context) -> {
                    ServerPlayer player = context.get().getSender();
                    if (player != null) {
                        slash(player, input.horizontal());
                    }
                })
                .add();
    }

    public static void sendSlash(boolean horizontal) {
        CHANNEL.sendToServer(new SlashInput(horizontal));
    }

    @Nullable
    public static InteractionHand heldHand(Player player) {
        InteractionHand right = player.getMainArm() == HumanoidArm.RIGHT
                ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (player.getItemInHand(right).is(ModItems.NIPPON_MARU.get())) {
            return right;
        }
        InteractionHand left = right == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        return player.getItemInHand(left).is(ModItems.NIPPON_MARU.get()) ? left : null;
    }

    @Override
    public boolean canBeHurtBy(DamageSource source) {
        return false;
    }

    @Override
    public int getEntityLifespan(ItemStack stack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.minecraftpokemon.nippon_maru.slashes"));
    }

    @SubscribeEvent
    public static void protectFromDamage(LivingAttackEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && heldHand(player) != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void protectFromDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && heldHand(player) != null) {
            player.setHealth(player.getMaxHealth());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void replenishHealth(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player
                && player.isAlive() && player.getHealth() < player.getMaxHealth() && heldHand(player) != null) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static void slash(ServerPlayer player, boolean horizontal) {
        InteractionHand hand = heldHand(player);
        if (hand == null || !player.isAlive() || player.isSpectator()
                || player.getCooldowns().isOnCooldown(ModItems.NIPPON_MARU.get())) {
            return;
        }

        ServerLevel level = player.serverLevel();
        Vec3 eye = player.getEyePosition();
        Vec3 forward = player.getLookAngle();
        Vec3 right = Vec3.directionFromRotation(0.0F, player.getYRot() + 90.0F);
        Vec3 across = horizontal ? right : right.cross(forward);
        double reach = player.getEntityReach();
        DamageSource damage = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(SLASH), player);

        player.getCooldowns().addCooldown(ModItems.NIPPON_MARU.get(), 1);
        player.swing(hand, true);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                horizontal ? SoundEvents.PLAYER_ATTACK_SWEEP : SoundEvents.PLAYER_ATTACK_CRIT,
                SoundSource.PLAYERS, 0.8F, 1.0F);

        Set<Mob> targets = new HashSet<>();
        for (int degrees = -40; degrees <= 40; degrees += 5) {
            double angle = Math.toRadians(degrees);
            Vec3 direction = forward.scale(Math.cos(angle)).add(across.scale(Math.sin(angle)));
            Vec3 end = eye.add(direction.scale(reach));
            HitResult obstacle = level.clip(new ClipContext(eye, end,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            Vec3 clearEnd = obstacle.getLocation();
            if (eye.distanceToSqr(clearEnd) > 1.6 * 1.6) {
                Vec3 trail = eye.add(direction.scale(1.6));
                level.sendParticles(ParticleTypes.END_ROD, trail.x, trail.y, trail.z, 1, 0, 0, 0, 0);
            }

            EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, eye, clearEnd,
                    player.getBoundingBox().expandTowards(clearEnd.subtract(eye)).inflate(1.0),
                    entity -> entity.isPickable() && mobFor(entity) != null, eye.distanceToSqr(clearEnd));
            if (hit != null && level.clip(new ClipContext(eye, hit.getEntity().getBoundingBox().getCenter(),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) {
                targets.add(mobFor(hit.getEntity()));
            }
        }
        for (Mob target : targets) {
            target.hurt(damage, Float.MAX_VALUE / 8.0F);
        }
    }

    @Nullable
    private static Mob mobFor(Entity entity) {
        Entity target = entity instanceof PartEntity<?> part ? part.getParent() : entity;
        return target instanceof Mob mob && mob.isAlive() ? mob : null;
    }

    private record SlashInput(boolean horizontal) {}
}
