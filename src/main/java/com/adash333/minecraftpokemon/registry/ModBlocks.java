package com.adash333.minecraftpokemon.registry;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.block.GachaBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MinecraftPokemonMod.MOD_ID);

    public static final RegistryObject<Block> BALL_GACHA = BLOCKS.register("ball_gacha",
            () -> new GachaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(2.5F)
                    .sound(SoundType.METAL), ModItems.MONSTER_BALL));

    public static final RegistryObject<Block> FOOD_GACHA = BLOCKS.register("food_gacha",
            () -> new GachaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PINK)
                    .strength(2.5F)
                    .sound(SoundType.METAL), ModItems.PEACH_BERRY, ModItems.RAINBOW_MANGO));

    public static final RegistryObject<Item> BALL_GACHA_ITEM = ModItems.ITEMS.register("ball_gacha",
            () -> new BlockItem(BALL_GACHA.get(), new Item.Properties()));

    public static final RegistryObject<Item> FOOD_GACHA_ITEM = ModItems.ITEMS.register("food_gacha",
            () -> new BlockItem(FOOD_GACHA.get(), new Item.Properties()));

    private ModBlocks() {
    }
}
