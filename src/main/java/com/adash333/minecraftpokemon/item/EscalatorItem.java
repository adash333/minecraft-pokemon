package com.adash333.minecraftpokemon.item;

import com.adash333.minecraftpokemon.block.EscalatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class EscalatorItem extends BlockItem {
    private static final String START = "minecraftpokemon:escalator_start";

    public EscalatorItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        CompoundTag data = player.getPersistentData();
        if (player.isShiftKeyDown()) {
            data.remove(START);
            player.displayClientMessage(Component.literal("エスカレーターの始点をリセットしました"), true);
            return InteractionResult.CONSUME;
        }
        BlockPlaceContext placement = new BlockPlaceContext(context);
        BlockPos end = placement.getClickedPos();
        if (!data.contains(START)) {
            InteractionResult result = super.useOn(context);
            if (result.consumesAction() && level.getBlockState(end).is(getBlock())) {
                CompoundTag start = new CompoundTag();
                start.putLong("pos", end.asLong());
                start.putString("dimension", level.dimension().location().toString());
                data.put(START, start);
                player.displayClientMessage(Component.literal("始点を設定しました。同じXかZ座標の終点にもう1個置いてください"), false);
            }
            return result;
        }
        CompoundTag saved = data.getCompound(START);
        BlockPos start = BlockPos.of(saved.getLong("pos"));
        if (!saved.getString("dimension").equals(level.dimension().location().toString())
                || !level.hasChunkAt(start) || !level.getBlockState(start).is(getBlock())) {
            data.remove(START);
            player.displayClientMessage(Component.literal("始点が見つかりません。もう一度始点を置いてください"), false);
            return InteractionResult.FAIL;
        }
        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();
        int length = Math.abs(dx) + Math.abs(dz);
        int rise = end.getY() - start.getY();
        if ((dx != 0 && dz != 0) || length == 0 || length > 128 || Math.abs(rise) > length) {
            player.displayClientMessage(Component.literal("終点は同じXかZ座標、128ブロック以内にしてください。高さの差以上の横の距離が必要です"), false);
            return InteractionResult.FAIL;
        }
        Direction travel = dx != 0 ? (dx > 0 ? Direction.EAST : Direction.WEST)
                : (dz > 0 ? Direction.SOUTH : Direction.NORTH);
        Direction uphill = rise < 0 ? travel.getOpposite() : travel;
        for (int i = 0; i <= length; i++) {
            BlockPos pos = start.relative(travel, i).offset(0, (int) Math.floor((double) rise * i / length), 0);
            if (!level.hasChunkAt(pos) || level.isOutsideBuildHeight(pos)
                    || !level.mayInteract(player, pos) || !player.mayUseItemAt(pos, Direction.UP, context.getItemInHand())
                    || (i != 0 && !level.getBlockState(pos).canBeReplaced())
                    || !level.getBlockState(pos.above()).canBeReplaced()
                    || !level.getBlockState(pos.above(2)).canBeReplaced()) {
                player.displayClientMessage(Component.literal("途中に障害物があるか、設置できない場所です。始点はそのままです"), false);
                return InteractionResult.FAIL;
            }
        }
        for (int i = 0; i <= length; i++) {
            BlockPos pos = start.relative(travel, i).offset(0, (int) Math.floor((double) rise * i / length), 0);
            int uphillIndex = rise < 0 ? i - 1 : i + 1;
            boolean flat = uphillIndex < 0 || uphillIndex > length
                    || (int) Math.floor((double) rise * uphillIndex / length) == pos.getY() - start.getY();
            BlockState state = getBlock().defaultBlockState().setValue(EscalatorBlock.FACING, uphill)
                    .setValue(EscalatorBlock.HALF, Half.BOTTOM).setValue(EscalatorBlock.SHAPE, StairsShape.STRAIGHT)
                    .setValue(EscalatorBlock.DESCENDING, rise < 0).setValue(EscalatorBlock.FLAT, flat);
            level.setBlock(pos, state, 3);
        }
        data.remove(START);
        if (!player.getAbilities().instabuild) context.getItemInHand().shrink(1);
        player.displayClientMessage(Component.literal("終点の高さまでエスカレーターができました！"), false);
        return InteractionResult.CONSUME;
    }
}
