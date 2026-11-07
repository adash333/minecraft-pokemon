package com.adash333.minecraftpokemon.client;

import com.adash333.minecraftpokemon.client.model.MewBlockModel;
import com.adash333.minecraftpokemon.client.model.DragonPokemonModel;
import com.adash333.minecraftpokemon.client.renderer.DragonPokemonRenderer;
import com.adash333.minecraftpokemon.client.renderer.MewBlockRenderer;
import com.adash333.minecraftpokemon.client.renderer.NipponMaruSheathLayer;
import com.adash333.minecraftpokemon.client.renderer.PlayerCharacterSkinLayer;
import com.adash333.minecraftpokemon.client.screen.VendingMachineScreen;
import com.adash333.minecraftpokemon.registry.ModEntities;
import com.adash333.minecraftpokemon.registry.ModItems;
import com.adash333.minecraftpokemon.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientSetup {
    private static final int PLATINUM_TINT = 0xE2EEFF;

    public static void register(IEventBus modBus) {
        modBus.addListener(ClientSetup::clientSetup);
        modBus.addListener(ClientSetup::registerLayerDefinitions);
        modBus.addListener(ClientSetup::addPlayerLayers);
        modBus.addListener(ClientSetup::registerAdditionalModels);
        modBus.addListener(ClientSetup::registerItemColors);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.MEW_BLOCK.get(), MewBlockRenderer::new);
            EntityRenderers.register(ModEntities.DRAGON_POKEMON.get(), DragonPokemonRenderer::new);
            EntityRenderers.register(ModEntities.MONSTER_BALL.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.BULLET.get(), context -> new ThrownItemRenderer<>(context, 0.35F, false));
            EntityRenderers.register(ModEntities.THROWN_SHURIKEN.get(), ThrownItemRenderer::new);
            MenuScreens.register(ModMenus.VENDING_MACHINE.get(), VendingMachineScreen::new);
            MinecraftForge.EVENT_BUS.addListener(PlayerCharacterSkinLayer::renderFirstPersonArm);
        });
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MewBlockModel.LAYER_LOCATION, MewBlockModel::createBodyLayer);
        event.registerLayerDefinition(DragonPokemonModel.LAYER_LOCATION, DragonPokemonModel::createBodyLayer);
    }

    private static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer classicRenderer = event.getSkin("default");
        if (classicRenderer != null) {
            classicRenderer.addLayer(PlayerCharacterSkinLayer.classic(classicRenderer));
            classicRenderer.addLayer(new NipponMaruSheathLayer(classicRenderer));
        }

        PlayerRenderer slimRenderer = event.getSkin("slim");
        if (slimRenderer != null) {
            slimRenderer.addLayer(PlayerCharacterSkinLayer.slim(slimRenderer));
            slimRenderer.addLayer(new NipponMaruSheathLayer(slimRenderer));
        }
    }

    private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(NipponMaruSheathLayer.MODEL);
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // Tint the iron base layer and leave any smithing trim in its own color.
        event.register((stack, layer) -> layer == 0 ? PLATINUM_TINT : -1,
                ModItems.PLATINUM_SWORD.get(), ModItems.PLATINUM_HELMET.get(), ModItems.PLATINUM_CHESTPLATE.get(),
                ModItems.PLATINUM_LEGGINGS.get(), ModItems.PLATINUM_BOOTS.get());
    }

    private ClientSetup() {
    }
}
