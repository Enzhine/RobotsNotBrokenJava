package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.Placeable2D;
import ru.enzhine.rnb.world.block.base.BiomeType;

public class WorldBiome implements Placeable2D {

    private final BiomeType biomeType;
    private final long x;
    private final long y;

    public WorldBiome(BiomeType biomeType, long x, long y) {
        this.biomeType = biomeType;
        this.x = x;
        this.y = y;
    }

    public BiomeType getBiomeType() {
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
}
