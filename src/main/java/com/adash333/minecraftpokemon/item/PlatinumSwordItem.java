package com.adash333.minecraftpokemon.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public final class PlatinumSwordItem extends SwordItem {
    private static final int FIRE_ASPECT_LEVEL = 6;

    public PlatinumSwordItem() {
        super(PlatinumMaterial.PLATINUM, 3, -2.4F, new Properties().fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setSecondsOnFire(FIRE_ASPECT_LEVEL * 4);
        return super.hurtEnemy(stack, target, attacker);
    }
}
