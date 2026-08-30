package com.adash333.minecraftpokemon.registry;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.item.DrinkItem;
import com.adash333.minecraftpokemon.item.MonsterBallItem;
import com.adash333.minecraftpokemon.item.PeachBerryItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftPokemonMod.MOD_ID);

    public static final RegistryObject<Item> MONSTER_BALL = ITEMS.register("monster_ball",
            () -> new MonsterBallItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> PEACH_BERRY = ITEMS.register("peach_berry",
            () -> new PeachBerryItem(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> RAINBOW_MANGO = ITEMS.register("rainbow_mango",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> HAMBURGER = ITEMS.register("hamburger",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(10)
                    .saturationMod(0.8F)
                    .build())));

    public static final RegistryObject<Item> FRENCH_FRIES = ITEMS.register("french_fries",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(0.5F)
                    .build())));

    public static final RegistryObject<Item> ICE_CREAM = ITEMS.register("ice_cream",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(5)
                    .saturationMod(0.4F)
                    .build())));

    public static final RegistryObject<Item> SALAD = ITEMS.register("salad",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.6F)
                    .build())));

    public static final RegistryObject<Item> CREEPER_COLA = ITEMS.register("creeper_cola",
            () -> new DrinkItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.3F)
                    .alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30 * 20, 3), 1.0F)
                    .build()), true));

    public static final RegistryObject<Item> MELON_PAN = ITEMS.register("melon_pan",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationMod(0.8F)
                    .build())));

    public static final RegistryObject<Item> MELON_SODA = ITEMS.register("melon_soda",
            () -> new DrinkItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.3F)
                    .alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 30 * 20), 1.0F)
                    .build()), true));

    private ModItems() {
    }
}
