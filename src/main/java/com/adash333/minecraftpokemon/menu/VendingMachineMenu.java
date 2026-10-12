package com.adash333.minecraftpokemon.menu;

import java.util.List;
import com.adash333.minecraftpokemon.registry.ModBlocks;
import com.adash333.minecraftpokemon.registry.ModItems;
import com.adash333.minecraftpokemon.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public class VendingMachineMenu extends AbstractContainerMenu {
    private static final List<RegistryObject<? extends Item>> PRODUCTS = List.of(
            ModItems.MELON_SODA,
            ModItems.CREEPER_COLA,
            ModItems.WATER,
            ModItems.HONEY_LEMON,
            ModItems.CHOCOLATE_DRINK,
            ModItems.ORANGE_JUICE,
            ModItems.APPLE_JUICE,
            ModItems.GRAPE_JUICE,
            ModItems.COFFEE);

    private static final int MAIN_INVENTORY_SIZE = 27;
    private static final int PLAYER_INVENTORY_SIZE = 36;

    private final Inventory playerInventory;
    private final ContainerLevelAccess access;

    public VendingMachineMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        this(containerId, inventory,
                ContainerLevelAccess.create(inventory.player.level(), data.readBlockPos()));
    }

    public VendingMachineMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenus.VENDING_MACHINE.get(), containerId);
        this.playerInventory = inventory;
        this.access = access;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9,
                        17 + column * 18, 145 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 17 + column * 18, 203));
        }
    }

    public int getProductCount() {
        return PRODUCTS.size();
    }

    public ItemStack getProduct(int index) {
        return new ItemStack(PRODUCTS.get(index).get());
    }

    public int getEmeraldCount() {
        return playerInventory.countItem(Items.EMERALD);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId < 0 || buttonId >= PRODUCTS.size() || !stillValid(player)) {
            return false;
        }

        if (!player.getAbilities().instabuild && !takeOneEmerald()) {
            player.displayClientMessage(Component.translatable(
                    "message.minecraftpokemon.vending.needs_emerald"), true);
            player.level().playSound(null, player.blockPosition(), SoundEvents.VILLAGER_NO,
                    SoundSource.PLAYERS, 0.7F, 1.0F);
            return false;
        }

        ItemStack drink = getProduct(buttonId);
        if (!playerInventory.add(drink)) {
            player.drop(drink, false);
        }
        player.displayClientMessage(Component.translatable(
                "message.minecraftpokemon.vending.bought", drink.getHoverName()), true);
        player.level().playSound(null, player.blockPosition(), SoundEvents.DISPENSER_DISPENSE,
                SoundSource.PLAYERS, 0.8F, 1.15F);
        broadcastChanges();
        return true;
    }

    private boolean takeOneEmerald() {
        for (int index = 0; index < playerInventory.getContainerSize(); index++) {
            ItemStack stack = playerInventory.getItem(index);
            if (stack.is(Items.EMERALD)) {
                stack.shrink(1);
                playerInventory.setChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.VENDING_MACHINE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        if (index < MAIN_INVENTORY_SIZE) {
            if (!moveItemStackTo(source, MAIN_INVENTORY_SIZE, PLAYER_INVENTORY_SIZE, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(source, 0, MAIN_INVENTORY_SIZE, false)) {
            return ItemStack.EMPTY;
        }

        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (source.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, source);
        return copy;
    }
}
