package ru.enzhine.rnb.world;

import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.gen.WorldBiome;

public interface WorldBiomeFactory {
    WorldBiome makeWorldBiome(Long x, Long y, BiomeType type);
}
