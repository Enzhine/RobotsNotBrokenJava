package ru.enzhine.rnb.world.gen.ores;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;

import java.util.Random;

public interface OreProcessor {
    void process(long x, long y, Chunk c, BiomeType biomeType, BlockFactory blockFactory, Random r);
}
