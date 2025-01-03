package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.Block;

import java.util.Random;
import java.util.List;

public interface BlockGenerator {
    Block getBlock(long x, long y, Chunk c, List<WorldBiome> worldBiomes, Random r);
}
