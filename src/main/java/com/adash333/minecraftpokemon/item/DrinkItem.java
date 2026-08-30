package com.adash333.minecraftpokemon.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DrinkItem extends Item {
    private final boolean returnsGlassBottle;

    public DrinkItem(Properties properties, boolean returnsGlassBottle) {
        super(properties);
        this.returnsGlassBottle = returnsGlassBottle;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack remainingStack = super.finishUsingItem(stack, level, entity);
        if (!returnsGlassBottle || !(entity instanceof Player player) || player.getAbilities().instabuild) {
            return remainingStack;
        }

        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
        if (remainingStack.isEmpty()) {
            return emptyBottle;
        }

        if (!player.getInventory().add(emptyBottle)) {
            player.drop(emptyBottle, false);
        }
        return remainingStack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
