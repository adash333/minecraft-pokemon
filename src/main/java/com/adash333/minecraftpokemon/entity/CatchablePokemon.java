package com.adash333.minecraftpokemon.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public interface CatchablePokemon {
    boolean isTamed();

    void tame(LivingEntity owner);

    void fleeFrom(LivingEntity entity);

    Component getName();
}
