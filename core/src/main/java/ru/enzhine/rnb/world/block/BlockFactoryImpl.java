package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;

public class BlockFactoryImpl implements BlockFactory {

    @Override
    public Block makeBlock(BlockType type, Long x, Long y, BiomeType biome, Chunk c) {
        Location newLoc = new Location((double)x, (double)y, c);
        return switch (type) {
            case DIRT -> new DirtBlock(newLoc, biome);
            case SAND -> new SandBlock(newLoc, biome);
            case SOFT_STONE -> new SoftStoneBlock(newLoc, biome);
            case HARD_STONE -> new HardStoneBlock(newLoc, biome);
            case STONE -> new StoneBlock(newLoc, biome);
        };
    }
}
