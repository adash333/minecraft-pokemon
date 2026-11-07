package com.adash333.minecraftpokemon.client;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.item.NipponMaruItem;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftPokemonMod.MOD_ID, value = Dist.CLIENT)
public final class NipponMaruControls {
    @SubscribeEvent
    public static void onMousePress(InputEvent.MouseButton.Pre event) {
        if (event.getAction() == InputConstants.PRESS
                && handlePress(InputConstants.Type.MOUSE.getOrCreate(event.getButton()))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if (event.getAction() == InputConstants.PRESS) {
            handlePress(InputConstants.getKey(event.getKey(), event.getScanCode()));
        }
    }

    @SubscribeEvent
    public static void suppressVanillaAction(InputEvent.InteractionKeyMappingTriggered event) {
        if ((event.isAttack() || event.isUseItem()) && isReady(Minecraft.getInstance())) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    private static boolean handlePress(InputConstants.Key key) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!isReady(minecraft)) {
            return false;
        }
        if (minecraft.options.keyAttack.isActiveAndMatches(key)) {
            NipponMaruItem.sendSlash(false);
        } else if (minecraft.options.keyUse.isActiveAndMatches(key)) {
            NipponMaruItem.sendSlash(true);
        } else {
            return false;
        }
        return true;
    }

    private static boolean isReady(Minecraft minecraft) {
        return minecraft.player != null && minecraft.player.isAlive() && !minecraft.player.isSpectator()
                && minecraft.screen == null && minecraft.getOverlay() == null && minecraft.mouseHandler.isMouseGrabbed()
                && NipponMaruItem.heldHand(minecraft.player) != null;
    }

    private NipponMaruControls() {}
}
