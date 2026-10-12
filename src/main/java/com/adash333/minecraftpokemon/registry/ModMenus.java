package com.adash333.minecraftpokemon.registry;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.menu.VendingMachineMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MinecraftPokemonMod.MOD_ID);

    public static final RegistryObject<MenuType<VendingMachineMenu>> VENDING_MACHINE =
            MENUS.register("vending_machine", () -> IForgeMenuType.create(VendingMachineMenu::new));

    private ModMenus() {
    }
}
