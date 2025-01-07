package ru.enzhine.rnb.world;

import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.Entity;
import ru.enzhine.rnb.world.entity.base.EntityType;

public interface World extends Ticking {
    int BLOCK_PIXEL_SIZE = 16;

    Entity summonEntity(EntityType type, double x, double y);

    void setBlock(BlockType type, Long x, Long y);

    Block getBlock(Long x, Long y, boolean loadChunk);

    Chunk getChunk(Long x, Long y, boolean loadChunk);

    int chunkSize();
}
