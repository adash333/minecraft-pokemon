package com.adash333.minecraftpokemon.client;

import com.adash333.minecraftpokemon.client.model.MewBlockModel;
import com.adash333.minecraftpokemon.client.model.DragonPokemonModel;
import com.adash333.minecraftpokemon.client.renderer.DragonPokemonRenderer;
import com.adash333.minecraftpokemon.client.renderer.MewBlockRenderer;
import com.adash333.minecraftpokemon.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientSetup {
    public static void register(IEventBus modBus) {
        modBus.addListener(ClientSetup::clientSetup);
        modBus.addListener(ClientSetup::registerLayerDefinitions);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.MEW_BLOCK.get(), MewBlockRenderer::new);
            EntityRenderers.register(ModEntities.DRAGON_POKEMON.get(), DragonPokemonRenderer::new);
            EntityRenderers.register(ModEntities.MONSTER_BALL.get(), ThrownItemRenderer::new);
        });
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MewBlockModel.LAYER_LOCATION, MewBlockModel::createBodyLayer);
        event.registerLayerDefinition(DragonPokemonModel.LAYER_LOCATION, DragonPokemonModel::createBodyLayer);
    }

    private ClientSetup() {
    }
}
