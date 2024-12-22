package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.block.base.BiomeFactory;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.Random;

public class BiomeFactoryImpl implements BiomeFactory {
    @Override
    public WorldBiome getBiome(Long x, Long y, Random r) {
        return switch (r.nextInt(BiomeType.values().length)) {
            case 0 -> new WorldBiome(BiomeType.RANDOM, x, y);
            case 1 -> new WorldBiome(BiomeType.RANDOM1, x, y);
            default -> throw new RuntimeException("Unknown biome!");
        };
    }
}
