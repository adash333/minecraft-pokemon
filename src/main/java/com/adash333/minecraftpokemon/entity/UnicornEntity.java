package com.adash333.minecraftpokemon.entity;

import com.adash333.minecraftpokemon.registry.ModEntities;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public final class UnicornEntity extends Horse {
    private static final int BABY_AGE = -24000;
    private static final int ROLL_DURATION = 24;
    private static final EntityDataAccessor<Integer> ROLL_TICKS =
            SynchedEntityData.defineId(UnicornEntity.class, EntityDataSerializers.INT);
    private UUID rollingFor;

    public UnicornEntity(EntityType<? extends Horse> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ROLL_TICKS, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag spawnTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, spawnType, groupData, spawnTag);
        setAge(BABY_AGE);
        return result;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        if (!(partner instanceof UnicornEntity unicorn)) {
            return super.getBreedOffspring(level, partner);
        }
        UnicornEntity foal = ModEntities.UNICORN.get().create(level);
        if (foal != null) {
            setOffspringAttributes(foal, unicorn);
        }
        return foal;
    }

    @Override
    protected boolean handleEating(Player player, ItemStack food) {
        // The custom carrot interaction is the only way to grow a foal.
        return !isBaby() && super.handleEating(player, food);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (isBaby() && held.is(Items.CARROT)) {
            if (!level().isClientSide) {
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
                setAge(0);
                playSound(SoundEvents.HORSE_EAT, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (!isBaby() && !isTamed()) {
            if (!held.isEmpty() || player.isSecondaryUseActive()) {
                return InteractionResult.PASS;
            }
            if (!level().isClientSide && onGround() && !isInWater() && entityData.get(ROLL_TICKS) == 0) {
                rollingFor = player.getUUID();
                getNavigation().stop();
                entityData.set(ROLL_TICKS, ROLL_DURATION);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        if (!level().isClientSide && getAge() == -1) {
            setAge(-2);
        }
        super.aiStep();
        if (!level().isClientSide && entityData.get(ROLL_TICKS) > 0) {
            int remaining = entityData.get(ROLL_TICKS) - 1;
            entityData.set(ROLL_TICKS, remaining);
            if (remaining == 0) {
                Player player = level().getPlayerByUUID(rollingFor);
                if (player != null) {
                    tameWithName(player);
                    if (player.isAlive() && !player.isSpectator() && !player.isPassenger()
                            && distanceToSqr(player) <= 16.0D) {
                        doPlayerRide(player);
                    }
                } else {
                    setOwnerUUID(rollingFor);
                    setTamed(true);
                    level().broadcastEntityEvent(this, (byte) 7);
                }
                rollingFor = null;
            }
        }
    }

    @Override
    public void travel(Vec3 input) {
        super.travel(entityData.get(ROLL_TICKS) > 0 ? Vec3.ZERO : input);
    }

    public float getRollProgress(float partialTick) {
        int remaining = entityData.get(ROLL_TICKS);
        return remaining == 0 ? 0.0F : Mth.clamp((ROLL_DURATION - remaining + partialTick) / ROLL_DURATION, 0.0F, 1.0F);
    }
}
