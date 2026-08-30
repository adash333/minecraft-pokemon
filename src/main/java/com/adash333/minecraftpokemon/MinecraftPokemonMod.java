package com.adash333.minecraftpokemon;

import com.adash333.minecraftpokemon.entity.MewBlockEntity;
import com.adash333.minecraftpokemon.entity.DragonPokemonEntity;
import com.adash333.minecraftpokemon.registry.ModBlocks;
import com.adash333.minecraftpokemon.registry.ModEntities;
import com.adash333.minecraftpokemon.registry.ModItems;
import com.adash333.minecraftpokemon.world.SpawnGachaPlacer;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftPokemonMod.MOD_ID)
public class MinecraftPokemonMod {
    public static final String MOD_ID = "minecraftpokemon";

    public MinecraftPokemonMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);

        modBus.addListener(this::addCreativeItems);
        modBus.addListener(this::registerAttributes);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> com.adash333.minecraftpokemon.client.ClientSetup.register(modBus));

        MinecraftForge.EVENT_BUS.register(new SpawnGachaPlacer());
    }

    private void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.MONSTER_BALL);
            event.accept(ModItems.PEACH_BERRY);
            event.accept(ModItems.RAINBOW_MANGO);
            event.accept(ModItems.HAMBURGER);
            event.accept(ModItems.FRENCH_FRIES);
            event.accept(ModItems.ICE_CREAM);
            event.accept(ModItems.SALAD);
            event.accept(ModItems.CREEPER_COLA);
            event.accept(ModItems.MELON_PAN);
            event.accept(ModItems.MELON_SODA);
            event.accept(ModBlocks.BALL_GACHA);
            event.accept(ModBlocks.FOOD_GACHA);
        }
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        AttributeSupplier attributes = MewBlockEntity.createAttributes().build();
        event.put(ModEntities.MEW_BLOCK.get(), attributes);
        event.put(ModEntities.DRAGON_POKEMON.get(), DragonPokemonEntity.createAttributes().build());
    }

}
