package com.adash333.minecraftpokemon.entity;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.registry.ModEntities;
import com.adash333.minecraftpokemon.registry.ModItems;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public final class WeaponProjectile extends ThrowableItemProjectile {
    private static final ResourceKey<DamageType> DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            new ResourceLocation(MinecraftPokemonMod.MOD_ID, "ranged_weapon"));
    private static final float BULLET_SPEED = 3.5F;
    @Nullable
    private UUID lockedTarget;
    private int flightTicks;

    public WeaponProjectile(EntityType<? extends WeaponProjectile> type, Level level) {
        super(type, level);
    }

    private WeaponProjectile(EntityType<WeaponProjectile> type, LivingEntity owner) {
        super(type, owner, owner.level());
    }

    public static WeaponProjectile bullet(LivingEntity shooter, @Nullable Entity target) {
        WeaponProjectile bullet = new WeaponProjectile(ModEntities.BULLET.get(), shooter);
        bullet.setNoGravity(true);
        Vec3 direction = shooter.getLookAngle();
        if (target != null) {
            bullet.lockedTarget = target.getUUID();
            direction = target.getBoundingBox().getCenter().subtract(bullet.position());
        }
        bullet.shoot(direction.x, direction.y, direction.z, BULLET_SPEED, 0.0F);
        return bullet;
    }

    public static WeaponProjectile shuriken(LivingEntity thrower) {
        WeaponProjectile shuriken = new WeaponProjectile(ModEntities.THROWN_SHURIKEN.get(), thrower);
        shuriken.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0, 1.8F, 0);
        return shuriken;
    }

    public static boolean canTarget(@Nullable Entity owner, Entity target) {
        Entity recipient = target instanceof PartEntity<?> part ? part.getParent() : target;
        return owner != null && recipient instanceof LivingEntity living && living.isAlive() && recipient != owner
                && (!(recipient instanceof Player player) || !player.isSpectator()
                        && (!(owner instanceof Player attacker) || attacker.canHarmPlayer(player)));
    }

    @Override
    public void tick() {
        if (level() instanceof ServerLevel server) {
            if (++flightTicks > 40) {
                discard();
                return;
            }
            if (lockedTarget != null) {
                Entity target = server.getEntity(lockedTarget);
                if (target == null || !canTarget(getOwner(), target)) {
                    lockedTarget = null;
                } else {
                    setDeltaMovement(target.getBoundingBox().getCenter().subtract(position())
                            .normalize().scale(BULLET_SPEED));
                }
            }
        } else if (getType() == ModEntities.BULLET.get()) {
            level().addParticle(ParticleTypes.CRIT, getX(), getY(), getZ(), 0, 0, 0);
        }
        super.tick();
    }

    @Override
    protected Item getDefaultItem() {
        return getType() == ModEntities.BULLET.get() ? Items.IRON_NUGGET : ModItems.SHURIKEN.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity owner = getOwner();
        Entity target = hit.getEntity();
        if (!(level() instanceof ServerLevel server) || !canTarget(owner, target)) {
            return;
        }
        if (server.clip(new ClipContext(position(), target.getBoundingBox().getCenter(),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS) {
            return;
        }

        Entity recipient = target instanceof PartEntity<?> part ? part.getParent() : target;
        LivingEntity living = (LivingEntity) recipient;
        float before = living.getHealth();
        boolean shuriken = getType() == ModEntities.THROWN_SHURIKEN.get();
        DamageSource source = new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DAMAGE), this, owner);
        if (target.hurt(source, shuriken ? 24.0F : 20.0F) && shuriken && owner instanceof LivingEntity thrower) {
            float healthLost = before - living.getHealth();
            if (healthLost > 0.0F) {
                thrower.heal(healthLost);
            }
        }
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!level().isClientSide) {
            discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (lockedTarget != null) {
            tag.putUUID("LockedTarget", lockedTarget);
        }
        tag.putInt("FlightTicks", flightTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        lockedTarget = tag.hasUUID("LockedTarget") ? tag.getUUID("LockedTarget") : null;
        flightTicks = tag.getInt("FlightTicks");
    }
}
