package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;

public interface ChunkGenerator {
    void fillChunk(Chunk c);

    BiomeType getBiome(long x, long y);

    void setBlockFactory(BlockFactory bf);

    BlockFactory getBlockFactory();
}
