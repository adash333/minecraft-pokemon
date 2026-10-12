package com.adash333.minecraftpokemon.client.screen;

import com.adash333.minecraftpokemon.menu.VendingMachineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class VendingMachineScreen extends AbstractContainerScreen<VendingMachineMenu> {
    private static final int PRODUCT_X = 12;
    private static final int PRODUCT_Y = 22;
    private static final int CELL_WIDTH = 54;
    private static final int CELL_HEIGHT = 30;
    private static final int CELL_GAP_X = 4;
    private static final int CELL_GAP_Y = 6;

    public VendingMachineScreen(VendingMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 194;
        imageHeight = 229;
        inventoryLabelY = 133;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        int hovered = productAt(mouseX, mouseY);
        if (hovered >= 0) {
            graphics.renderTooltip(font, menu.getProduct(hovered), mouseX, mouseY);
        } else {
            renderTooltip(graphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;
        graphics.fill(left, top, left + imageWidth, top + imageHeight, 0xFFF3E8DA);
        graphics.fill(left, top, left + imageWidth, top + 18, 0xFFC52127);
        graphics.fill(left + 6, top + 128, left + imageWidth - 6, top + imageHeight - 6, 0xFFD8C8B5);
        drawBorder(graphics, left, top, imageWidth, imageHeight, 0xFF681015);

        for (int index = 0; index < menu.getProductCount(); index++) {
            int column = index % 3;
            int row = index / 3;
            int x = left + PRODUCT_X + column * (CELL_WIDTH + CELL_GAP_X);
            int y = top + PRODUCT_Y + row * (CELL_HEIGHT + CELL_GAP_Y);
            boolean hovered = inside(mouseX, mouseY, x, y, CELL_WIDTH, CELL_HEIGHT);
            graphics.fill(x, y, x + CELL_WIDTH, y + CELL_HEIGHT,
                    hovered ? 0xFFFFF0B5 : 0xFFCAE8F1);
            drawBorder(graphics, x, y, CELL_WIDTH, CELL_HEIGHT,
                    hovered ? 0xFFFFB000 : 0xFF526A73);

            ItemStack product = menu.getProduct(index);
            graphics.renderItem(product, x + 5, y + 7);
            graphics.renderItem(new ItemStack(Items.EMERALD), x + 32, y + 7);
            graphics.drawString(font, "1", x + 47, y + 11, 0xFF243128, false);
        }

        drawPlayerInventorySlots(graphics, left, top);
    }

    private void drawPlayerInventorySlots(GuiGraphics graphics, int left, int top) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlot(graphics, left + 16 + column * 18, top + 144 + row * 18);
            }
        }
        for (int column = 0; column < 9; column++) {
            drawSlot(graphics, left + 16 + column * 18, top + 202);
        }
    }

    private static void drawSlot(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x + 18, y + 18, 0xFF706354);
        graphics.fill(x + 1, y + 1, x + 17, y + 17, 0xFFF0E3D2);
    }

    private static void drawBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + 1, color);
        graphics.fill(x, y + height - 1, x + width, y + height, color);
        graphics.fill(x, y, x + 1, y + height, color);
        graphics.fill(x + width - 1, y, x + width, y + height, color);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0xFFFFFFFF, false);
        Component emeralds = Component.translatable(
                "gui.minecraftpokemon.vending_machine.emeralds", menu.getEmeraldCount());
        graphics.drawString(font, emeralds, imageWidth - 8 - font.width(emeralds), 6,
                0xFFFFFFFF, false);
        graphics.drawString(font, Component.translatable(
                "gui.minecraftpokemon.vending_machine.instructions"), 12, 120, 0xFF552329, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY,
                0xFF40352B, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int product = productAt(mouseX, mouseY);
        if (button == 0 && product >= 0 && minecraft != null && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, product);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private int productAt(double mouseX, double mouseY) {
        for (int index = 0; index < menu.getProductCount(); index++) {
            int column = index % 3;
            int row = index / 3;
            int x = leftPos + PRODUCT_X + column * (CELL_WIDTH + CELL_GAP_X);
            int y = topPos + PRODUCT_Y + row * (CELL_HEIGHT + CELL_GAP_Y);
            if (inside(mouseX, mouseY, x, y, CELL_WIDTH, CELL_HEIGHT)) {
                return index;
            }
        }
        return -1;
    }

    private static boolean inside(double mouseX, double mouseY,
            int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
