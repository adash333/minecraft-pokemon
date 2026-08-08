package com.adash333.minecraftpokemon.entity;

import com.adash333.minecraftpokemon.registry.ModItems;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MewBlockEntity extends PathfinderMob implements CatchablePokemon {
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(MewBlockEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> TAMED =
            SynchedEntityData.defineId(MewBlockEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EVOLVED =
            SynchedEntityData.defineId(MewBlockEntity.class, EntityDataSerializers.BOOLEAN);

    private int fleeTicks;

    public MewBlockEntity(EntityType<? extends MewBlockEntity> type, Level level) {
        super(type, level);
        setNoGravity(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10000.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(4, new RandomStrollGoal(this, 0.9D));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(OWNER, Optional.empty());
        entityData.define(TAMED, false);
        entityData.define(EVOLVED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            return;
        }

        if (fleeTicks > 0) {
            fleeTicks--;
        }

        Player owner = getOwnerPlayer();
        if (owner != null && distanceToSqr(owner) > 100.0D) {
            getNavigation().moveTo(owner, 1.25D);
            if (distanceToSqr(owner) > 576.0D) {
                teleportTo(owner.getX() + 1.5D, owner.getY(), owner.getZ() + 1.5D);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ModItems.PEACH_BERRY.get())) {
            return super.mobInteract(player, hand);
        }

        if (!isTamedBy(player)) {
            if (!level().isClientSide) {
                player.displayClientMessage(net.minecraft.network.chat.Component.literal("まだなついていないので、先にモンスターボールで捕まえよう！"), true);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if (!level().isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            playHappyEffects();
            if (!isEvolved() && random.nextFloat() < 0.25F) {
                setEvolved(true);
                level().playSound(null, blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 1.3F);
                player.displayClientMessage(net.minecraft.network.chat.Component.literal("ミュウブロックが少し大きく進化した！"), true);
            }
        }

        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    public boolean isTamed() {
        return entityData.get(TAMED);
    }

    public boolean isEvolved() {
        return entityData.get(EVOLVED);
    }

    public void setEvolved(boolean evolved) {
        entityData.set(EVOLVED, evolved);
    }

    public boolean isTamedBy(Player player) {
        return isTamed() && entityData.get(OWNER).map(player.getUUID()::equals).orElse(false);
    }

    public void tame(LivingEntity owner) {
        entityData.set(TAMED, true);
        entityData.set(OWNER, Optional.of(owner.getUUID()));
        fleeTicks = 0;
        playHappyEffects();
    }

    public void fleeFrom(LivingEntity entity) {
        fleeTicks = 80;
        Vec3 away = position().subtract(entity.position()).normalize().scale(12.0D);
        getNavigation().moveTo(getX() + away.x, getY(), getZ() + away.z, 1.8D);
    }

    public Player getOwnerPlayer() {
        Optional<UUID> owner = entityData.get(OWNER);
        if (owner.isEmpty()) {
            return null;
        }
        return level().getPlayerByUUID(owner.get());
    }

    private void playHappyEffects() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        level().playSound(null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 1.0F, 1.4F);
        for (int i = 0; i < 10; i++) {
            serverLevel.sendParticles(
                    ParticleTypes.HEART,
                    getX() + (random.nextDouble() - 0.5D) * 0.7D,
                    getY() + 1.0D + random.nextDouble() * 0.4D,
                    getZ() + (random.nextDouble() - 0.5D) * 0.7D,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Tamed", isTamed());
        tag.putBoolean("Evolved", isEvolved());
        entityData.get(OWNER).ifPresent(uuid -> tag.putUUID("Owner", uuid));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(TAMED, tag.getBoolean("Tamed"));
        entityData.set(EVOLVED, tag.getBoolean("Evolved"));
        if (tag.hasUUID("Owner")) {
            entityData.set(OWNER, Optional.of(tag.getUUID("Owner")));
        }
    }
}
