package ru.enzhine.rnb.world.block.base;

import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.Random;

public interface BiomeFactory {
    WorldBiome getBiome(Long x, Long y, Random r);
}
