package com.adash333.minecraftpokemon.world;

import com.adash333.minecraftpokemon.entity.MewBlockEntity;
import com.adash333.minecraftpokemon.entity.DragonPokemonEntity;
import com.adash333.minecraftpokemon.registry.ModBlocks;
import com.adash333.minecraftpokemon.registry.ModEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpawnGachaPlacer {
    private static final String DATA_NAME = "minecraftpokemon_gacha";
    private static final int MEW_SPAWN_INTERVAL_TICKS = 20 * 20;
    private static final int MINIRYU_SPAWN_INTERVAL_TICKS = 15 * 20;
    private static final int MINIRYU_SPAWN_COUNT = 2;
    private static final int MEW_CLEANUP_THRESHOLD = 100;
    private static final int DRAGON_POKEMON_CLEANUP_THRESHOLD = 100;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().overworld();
        GachaWorldData data = level.getDataStorage().computeIfAbsent(GachaWorldData::load, GachaWorldData::new, DATA_NAME);
        if (data.isPlaced()) {
            return;
        }

        BlockPos spawn = level.getSharedSpawnPos();
        BlockPos base = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawn).above();

        placeGacha(level, base.offset(2, 0, 0), true);
        placeGacha(level, base.offset(5, 0, 0), false);

        for (int i = 0; i < 5; i++) {
            MewBlockEntity mew = ModEntities.MEW_BLOCK.get().create(level);
            if (mew != null) {
                BlockPos pos = base.offset(-4 + i * 2, 0, 6 + (i % 2));
                mew.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
                level.addFreshEntity(mew);
            }
        }

        data.setPlaced();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level) || player.tickCount <= 0) {
            return;
        }

        if (player.tickCount % MEW_SPAWN_INTERVAL_TICKS == 0) {
            spawnMewNearPlayer(level, player);
        }
        if (player.tickCount % MINIRYU_SPAWN_INTERVAL_TICKS == 0) {
            for (int i = 0; i < MINIRYU_SPAWN_COUNT; i++) {
                spawnMiniryuNearPlayer(level, player);
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (event.getEntity() instanceof MewBlockEntity joiningMew) {
            cleanupUntamedMews(level, joiningMew, event);
        } else if (event.getEntity() instanceof DragonPokemonEntity joiningDragonPokemon) {
            cleanupUntamedDragonPokemon(level, joiningDragonPokemon, event);
        }
    }

    private void cleanupUntamedMews(ServerLevel level, MewBlockEntity joiningMew, EntityJoinLevelEvent event) {
        List<MewBlockEntity> mews = new ArrayList<>();
        for (ServerLevel serverLevel : level.getServer().getAllLevels()) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof MewBlockEntity mew) {
                    mews.add(mew);
                }
            }
        }

        if (!mews.contains(joiningMew)) {
            mews.add(joiningMew);
        }
        if (mews.size() < MEW_CLEANUP_THRESHOLD) {
            return;
        }

        for (MewBlockEntity mew : mews) {
            if (mew != joiningMew && !mew.isTamed()) {
                mew.discard();
            }
        }
        if (!joiningMew.isTamed()) {
            event.setCanceled(true);
        }
    }

    private void cleanupUntamedDragonPokemon(ServerLevel level, DragonPokemonEntity joiningDragonPokemon,
            EntityJoinLevelEvent event) {
        List<DragonPokemonEntity> dragonPokemon = new ArrayList<>();
        for (ServerLevel serverLevel : level.getServer().getAllLevels()) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof DragonPokemonEntity dragon) {
                    dragonPokemon.add(dragon);
                }
            }
        }

        if (!dragonPokemon.contains(joiningDragonPokemon)) {
            dragonPokemon.add(joiningDragonPokemon);
        }
        if (dragonPokemon.size() < DRAGON_POKEMON_CLEANUP_THRESHOLD) {
            return;
        }

        for (DragonPokemonEntity dragon : dragonPokemon) {
            if (dragon != joiningDragonPokemon && !dragon.isTamed()) {
                dragon.discard();
            }
        }
        if (!joiningDragonPokemon.isTamed()) {
            event.setCanceled(true);
        }
    }

    private void placeGacha(ServerLevel level, BlockPos pos, boolean ball) {
        BlockPos floor = pos.below();
        if (level.isEmptyBlock(floor)) {
            level.setBlock(floor, Blocks.SMOOTH_STONE.defaultBlockState(), 3);
        }
        level.setBlock(pos, ball ? ModBlocks.BALL_GACHA.get().defaultBlockState() : ModBlocks.FOOD_GACHA.get().defaultBlockState(), 3);
        level.setBlock(pos.above(), Blocks.GLASS.defaultBlockState(), 3);
    }

    private void spawnMewNearPlayer(ServerLevel level, ServerPlayer player) {
        int xOffset = level.random.nextInt(17) - 8;
        int zOffset = level.random.nextInt(17) - 8;
        if (Math.abs(xOffset) < 4 && Math.abs(zOffset) < 4) {
            xOffset += xOffset < 0 ? -6 : 6;
            zOffset += zOffset < 0 ? -6 : 6;
        }

        BlockPos target = player.blockPosition().offset(xOffset, 0, zOffset);
        BlockPos spawnPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, target);
        MewBlockEntity mew = ModEntities.MEW_BLOCK.get().create(level);
        if (mew == null) {
            return;
        }

        mew.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
        level.addFreshEntity(mew);
    }

    private void spawnMiniryuNearPlayer(ServerLevel level, ServerPlayer player) {
        int xOffset = level.random.nextInt(21) - 10;
        int zOffset = level.random.nextInt(21) - 10;
        if (Math.abs(xOffset) < 4 && Math.abs(zOffset) < 4) {
            xOffset += xOffset < 0 ? -6 : 6;
            zOffset += zOffset < 0 ? -6 : 6;
        }

        BlockPos target = player.blockPosition().offset(xOffset, 0, zOffset);
        BlockPos spawnPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, target);
        DragonPokemonEntity miniryu = ModEntities.DRAGON_POKEMON.get().create(level);
        if (miniryu == null) {
            return;
        }

        miniryu.setStage(DragonPokemonEntity.MINIRYU_STAGE);
        miniryu.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D,
                level.random.nextFloat() * 360.0F, 0.0F);
        level.addFreshEntity(miniryu);
    }
}
