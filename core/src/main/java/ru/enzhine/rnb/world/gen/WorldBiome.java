package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.adt.PlaceableValue2D;
import ru.enzhine.rnb.world.block.base.BiomeType;

public class WorldBiome implements PlaceableValue2D<BiomeType> {

    private final BiomeType biomeType;
    private final long x;
    private final long y;

    public WorldBiome(BiomeType biomeType, long x, long y) {
        this.biomeType = biomeType;
        this.x = x;
        this.y = y;
    }

    public BiomeType get() {
        return biomeType;
    }

    @Override
    public Long getX() {
        return x;
    }

    @Override
    public Long getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("%s at %d;%d", biomeType.toString(), x, y);
    }
}
