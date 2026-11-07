package com.adash333.minecraftpokemon.item;

import com.adash333.minecraftpokemon.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum PlatinumMaterial implements ArmorMaterial, Tier {
    PLATINUM;

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 13 * 42;
            case CHESTPLATE -> 16 * 42;
            case LEGGINGS -> 15 * 42;
            case BOOTS -> 11 * 42;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type) {
            case HELMET, BOOTS -> 4;
            case CHESTPLATE -> 9;
            case LEGGINGS -> 7;
        };
    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.PLATINUM_INGOT.get());
    }

    @Override
    public String getName() {
        return "minecraftpokemon:platinum";
    }

    @Override
    public float getToughness() {
        return 4.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.12F;
    }

    @Override
    public int getUses() {
        return 2400;
    }

    @Override
    public float getSpeed() {
        return 9.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 5.0F;
    }

    @Override
    public int getLevel() {
        return 4;
    }
}
