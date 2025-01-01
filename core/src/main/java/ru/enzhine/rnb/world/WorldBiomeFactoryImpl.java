package ru.enzhine.rnb.world;

import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.gen.WorldBiome;

public class WorldBiomeFactoryImpl implements WorldBiomeFactory {
    @Override
    public WorldBiome makeWorldBiome(Long x, Long y, BiomeType type) {
        return new WorldBiome(type, x, y);
    }
}
