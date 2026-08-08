package com.adash333.minecraftpokemon.registry;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.item.MonsterBallItem;
import com.adash333.minecraftpokemon.item.PeachBerryItem;
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

    private ModItems() {
    }
}
