package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.gen.ores.OreProcessor;

import java.util.List;
import java.util.Random;

public interface OreProcessorGenerator {
    OreProcessor getOreProcessor(long x, long y, List<WorldBiome> worldBiomes, Random random);
}
