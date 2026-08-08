package com.adash333.minecraftpokemon.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class GachaWorldData extends SavedData {
    private boolean placed;

    public static GachaWorldData load(CompoundTag tag) {
        GachaWorldData data = new GachaWorldData();
        data.placed = tag.getBoolean("Placed");
        return data;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced() {
        placed = true;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Placed", placed);
        return tag;
    }
}
