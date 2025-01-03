package ru.enzhine.rnb.world.block.base;

import ru.enzhine.rnb.world.Chunk;

public interface BlockFactory {

    Block makeBlock(BlockType type, long x, long y, BiomeType biome, Chunk c);
}
