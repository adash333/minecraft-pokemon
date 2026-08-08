package com.adash333.minecraftpokemon.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;

public class GachaBlock extends Block {
    private final List<RegistryObject<? extends Item>> prizes;

    @SafeVarargs
    public GachaBlock(Properties properties, RegistryObject<? extends Item>... prizes) {
        super(properties);
        if (prizes.length == 0) {
            throw new IllegalArgumentException("A gacha must have at least one prize");
        }
        this.prizes = List.of(prizes);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        RegistryObject<? extends Item> prize = prizes.get(level.random.nextInt(prizes.size()));
        ItemStack stack = new ItemStack(prize.get());
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }

        level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.8F, 1.2F);
        player.displayClientMessage(Component.literal("ガチャから " + stack.getHoverName().getString() + " が出た！"), true);
        return InteractionResult.CONSUME;
    }
}
