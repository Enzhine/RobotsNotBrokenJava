package ru.enzhine.rnb.world.gen.ores;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.List;
import java.util.Random;

public interface OreProcessor {
    void process(long x, long y, Chunk c, List<WorldBiome> worldBiomes, Random r);
}
