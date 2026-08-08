package com.adash333.minecraftpokemon.entity;

import com.adash333.minecraftpokemon.registry.ModItems;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DragonPokemonEntity extends PathfinderMob implements CatchablePokemon {
    public static final int MINIRYU_STAGE = 0;
    public static final int HAKURYU_STAGE = 1;
    public static final int KAIRYU_STAGE = 2;

    private static final EntityDataAccessor<Integer> STAGE =
            SynchedEntityData.defineId(DragonPokemonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(DragonPokemonEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> TAMED =
            SynchedEntityData.defineId(DragonPokemonEntity.class, EntityDataSerializers.BOOLEAN);

    private int fleeTicks;

    public DragonPokemonEntity(EntityType<? extends DragonPokemonEntity> type, Level level) {
        super(type, level);
        moveControl = new FlyingMoveControl(this, 12, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(3, new KairyuFlyingGoal(this, 1.0D));
        goalSelector.addGoal(4, new GroundStrollGoal(this, 0.9D));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 10.0F));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STAGE, MINIRYU_STAGE);
        entityData.define(OWNER, Optional.empty());
        entityData.define(TAMED, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    public int getStage() {
        return entityData.get(STAGE);
    }

    public void setStage(int stage) {
        entityData.set(STAGE, Mth.clamp(stage, MINIRYU_STAGE, KAIRYU_STAGE));
        setNoGravity(getStage() == KAIRYU_STAGE);
    }

    @Override
    public void tick() {
        super.tick();
        boolean canFly = getStage() == KAIRYU_STAGE;
        setNoGravity(canFly);

        if (level().isClientSide) {
            return;
        }

        if (fleeTicks > 0) {
            fleeTicks--;
        }

        Player owner = getOwnerPlayer();
        if (owner == null || fleeTicks > 0) {
            return;
        }

        double targetY = canFly ? owner.getY() + 2.0D : owner.getY();
        if (distanceToSqr(owner) > 100.0D) {
            getNavigation().moveTo(owner.getX(), targetY, owner.getZ(), canFly ? 1.15D : 1.0D);
        }
        if (distanceToSqr(owner) > 576.0D) {
            teleportTo(owner.getX() + 1.5D, targetY, owner.getZ() + 1.5D);
        }
    }

    public Component getStageName() {
        return switch (getStage()) {
            case HAKURYU_STAGE -> Component.translatable("entity.minecraftpokemon.hakuryu");
            case KAIRYU_STAGE -> Component.translatable("entity.minecraftpokemon.kairyu");
            default -> Component.translatable("entity.minecraftpokemon.miniryu");
        };
    }

    @Override
    public Component getName() {
        return getStageName();
    }

    @Override
    public boolean isTamed() {
        return entityData.get(TAMED);
    }

    public boolean isTamedBy(Player player) {
        return isTamed() && entityData.get(OWNER).map(player.getUUID()::equals).orElse(false);
    }

    @Override
    public void tame(net.minecraft.world.entity.LivingEntity owner) {
        entityData.set(TAMED, true);
        entityData.set(OWNER, Optional.of(owner.getUUID()));
        fleeTicks = 0;
        playHappyEffects();
    }

    @Override
    public void fleeFrom(net.minecraft.world.entity.LivingEntity entity) {
        fleeTicks = 80;
        Vec3 away = position().subtract(entity.position()).normalize().scale(14.0D);
        double targetY = getStage() == KAIRYU_STAGE ? getY() + 5.0D : getY();
        getNavigation().moveTo(getX() + away.x, targetY, getZ() + away.z, 1.5D);
    }

    public Player getOwnerPlayer() {
        Optional<UUID> owner = entityData.get(OWNER);
        if (owner.isEmpty()) {
            return null;
        }
        return level().getPlayerByUUID(owner.get());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ModItems.RAINBOW_MANGO.get())) {
            return super.mobInteract(player, hand);
        }

        if (level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (getStage() >= KAIRYU_STAGE) {
            player.displayClientMessage(Component.translatable("message.minecraftpokemon.fully_evolved", getStageName()), true);
            return InteractionResult.CONSUME;
        }

        Component previousName = getStageName();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        setStage(getStage() + 1);
        setHealth(getMaxHealth());
        playEvolutionEffects();
        player.displayClientMessage(
                Component.translatable("message.minecraftpokemon.evolved", previousName, getStageName()), true);
        return InteractionResult.CONSUME;
    }

    private void playEvolutionEffects() {
        level().playSound(null, blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 1.15F);
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(ParticleTypes.END_ROD, getX(), getY() + 1.0D, getZ(),
                30, 0.7D, 0.8D, 0.7D, 0.08D);
        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getY() + 0.8D, getZ(),
                20, 0.8D, 0.6D, 0.8D, 0.05D);
    }

    private void playHappyEffects() {
        level().playSound(null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.NEUTRAL, 1.0F, 1.4F);
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HEART, getX(), getY() + 1.2D, getZ(),
                    12, 0.6D, 0.6D, 0.6D, 0.05D);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("DragonStage", getStage());
        tag.putBoolean("Tamed", isTamed());
        entityData.get(OWNER).ifPresent(uuid -> tag.putUUID("Owner", uuid));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setStage(tag.getInt("DragonStage"));
        entityData.set(TAMED, tag.getBoolean("Tamed"));
        if (tag.hasUUID("Owner")) {
            entityData.set(OWNER, Optional.of(tag.getUUID("Owner")));
        }
    }

    private static class GroundStrollGoal extends RandomStrollGoal {
        private final DragonPokemonEntity dragon;

        GroundStrollGoal(DragonPokemonEntity dragon, double speedModifier) {
            super(dragon, speedModifier);
            this.dragon = dragon;
        }

        @Override
        public boolean canUse() {
            return dragon.getStage() != KAIRYU_STAGE && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return dragon.getStage() != KAIRYU_STAGE && super.canContinueToUse();
        }
    }

    private static class KairyuFlyingGoal extends WaterAvoidingRandomFlyingGoal {
        private final DragonPokemonEntity dragon;

        KairyuFlyingGoal(DragonPokemonEntity dragon, double speedModifier) {
            super(dragon, speedModifier);
            this.dragon = dragon;
        }

        @Override
        public boolean canUse() {
            return dragon.getStage() == KAIRYU_STAGE && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return dragon.getStage() == KAIRYU_STAGE && super.canContinueToUse();
        }
    }
}
