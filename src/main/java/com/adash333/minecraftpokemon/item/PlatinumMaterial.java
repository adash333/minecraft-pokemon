package com.adash333.minecraftpokemon.item;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public enum PlatinumMaterial implements ArmorMaterial, Tier {
    PLATINUM;

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return ArmorMaterials.NETHERITE.getDurabilityForType(type);
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return ArmorMaterials.NETHERITE.getDefenseForType(type);
    }

    @Override
    public int getEnchantmentValue() {
        return ArmorMaterials.NETHERITE.getEnchantmentValue();
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.IRON.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.PLATINUM_INGOT.get());
    }

    @Override
    public String getName() {
        return MinecraftPokemonMod.MOD_ID + ":platinum";
    }

    @Override
    public float getToughness() {
        return ArmorMaterials.NETHERITE.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return ArmorMaterials.NETHERITE.getKnockbackResistance();
    }

    @Override
    public int getUses() {
        return Tiers.NETHERITE.getUses();
    }

    @Override
    public float getSpeed() {
        return Tiers.NETHERITE.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
        return Tiers.NETHERITE.getAttackDamageBonus();
    }

    @Override
    public int getLevel() {
        return Tiers.NETHERITE.getLevel();
    }
}
