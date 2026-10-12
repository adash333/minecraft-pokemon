package com.adash333.minecraftpokemon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Place successive steps one block higher in the facing direction. */
public class EscalatorBlock extends StairBlock {
    public static final BooleanProperty DESCENDING = BooleanProperty.create("descending");
    public static final BooleanProperty FLAT = BooleanProperty.create("flat");

    public EscalatorBlock(BlockState base, Properties properties) {
        super(base, properties);
        registerDefaultState(defaultBlockState().setValue(DESCENDING, false).setValue(FLAT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DESCENDING);
        builder.add(FLAT);
    }

    @Override
    public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state,
            net.minecraft.world.level.BlockGetter level, BlockPos pos,
            net.minecraft.world.phys.shapes.CollisionContext context) {
        return state.getValue(FLAT) ? net.minecraft.world.phys.shapes.Shapes.block()
                : super.getShape(state, level, pos, context);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (entity.isShiftKeyDown() || !entity.onGround() || state.getValue(HALF) != Half.BOTTOM) {
            return;
        }
        Direction direction = state.getValue(FACING);
        boolean descending = state.getValue(DESCENDING);
        double speed = descending ? -0.08D : 0.08D;
        entity.move(MoverType.SELF, new Vec3(direction.getStepX() * speed,
                descending || state.getValue(FLAT) ? 0.0D : 0.08D, direction.getStepZ() * speed));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockState updated = state.cycle(DESCENDING);
            level.setBlock(pos, updated, 3);
            player.displayClientMessage(Component.translatable(updated.getValue(DESCENDING)
                    ? "message.minecraftpokemon.escalator.down" : "message.minecraftpokemon.escalator.up"), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
