package com.adash333.minecraftpokemon.entity;

import com.adash333.minecraftpokemon.registry.ModEntities;
import com.adash333.minecraftpokemon.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MonsterBallEntity extends ThrowableItemProjectile {
    public MonsterBallEntity(EntityType<? extends MonsterBallEntity> type, Level level) {
        super(type, level);
    }

    public MonsterBallEntity(Level level, LivingEntity owner) {
        super(ModEntities.MONSTER_BALL.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MONSTER_BALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (level().isClientSide) {
            return;
        }

        if (!(hit.getEntity() instanceof CatchablePokemon pokemon) || pokemon.isTamed()) {
            return;
        }

        if (!(getOwner() instanceof LivingEntity owner)) {
            return;
        }

        boolean caught = random.nextFloat() < 0.7F;
        if (caught) {
            pokemon.tame(owner);
            owner.sendSystemMessage(Component.translatable("message.minecraftpokemon.caught", pokemon.getName()));
        } else {
            pokemon.fleeFrom(owner);
            owner.sendSystemMessage(Component.translatable("message.minecraftpokemon.escaped", pokemon.getName()));
        }
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!level().isClientSide) {
            discard();
        }
    }
}
