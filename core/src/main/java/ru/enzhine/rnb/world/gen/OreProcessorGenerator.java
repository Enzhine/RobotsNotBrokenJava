package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.gen.ores.OreProcessor;

import java.util.Random;

public interface OreProcessorGenerator {
    OreProcessor getOreProcessor(long x, long y, BiomeType biomeType, Random random);
}
