package com.adash333.minecraftpokemon.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

/** Keeps the specified Pokemon outside every player's 20-block radius. */
public final class PlayerSeparation {
    public static boolean isClear(PathfinderMob mob, Vec3 position) {
        return mob.level().players().stream().noneMatch(player -> player.isAlive()
                && player.position().distanceToSqr(position) <= 400.0D);
    }

    public static Vec3 findPosition(PathfinderMob mob, Vec3 center, boolean flying) {
        double startAngle = mob.getRandom().nextDouble() * Math.PI * 2.0D;
        for (int i = 0; i < 32; i++) {
            double angle = startAngle + i * Math.PI / 8.0D;
            double radius = i < 16 ? 24.0D : 32.0D;
            BlockPos column = BlockPos.containing(center.x + Math.cos(angle) * radius,
                    center.y, center.z + Math.sin(angle) * radius);
            if (!mob.level().hasChunkAt(column)) continue;
            BlockPos ground = mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column);
            Vec3 target = new Vec3(ground.getX() + 0.5D,
                    ground.getY() + (flying ? 1.5D : 0.0D), ground.getZ() + 0.5D);
            if (isClear(mob, target) && mob.level().getWorldBorder().isWithinBounds(BlockPos.containing(target))
                    && mob.level().noCollision(mob, mob.getBoundingBox().move(target.subtract(mob.position())))
                    && mob.level().getFluidState(ground).isEmpty()
                    && !mob.level().getBlockState(ground.below()).getCollisionShape(mob.level(), ground.below()).isEmpty()) {
                return target;
            }
        }
        return null;
    }

    public static void tick(PathfinderMob mob, boolean flying) {
        Player nearest = null;
        double distance = 24.0D * 24.0D;
        for (Player player : mob.level().players()) {
            double candidate = mob.distanceToSqr(player);
            if (player.isAlive() && candidate < distance) {
                nearest = player;
                distance = candidate;
            }
        }
        if (nearest == null) return;
        if (distance > 400.0D && mob.tickCount % 10 != 0) return;
        Vec3 target = findPosition(mob, nearest.position(), flying);
        mob.getNavigation().stop();
        if (target == null) {
            mob.setDeltaMovement(Vec3.ZERO);
            return;
        }
        if (distance <= 400.0D) {
            // Also handles existing saves and a player running toward a Pokemon.
            mob.teleportTo(target.x, target.y, target.z);
            mob.setDeltaMovement(Vec3.ZERO);
        } else {
            mob.getNavigation().moveTo(target.x, target.y, target.z, 1.4D);
        }
    }

    private PlayerSeparation() { }
}
