package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;

public class BlockFactoryImpl implements BlockFactory {

    @Override
    public Block makeBlock(BlockType type, Long x, Long y, BiomeType biome, Chunk c) {
        Location newLoc = new Location((double)x, (double)y, c);
        switch (type){
            case DIRT:
                return new DirtBlock(newLoc, biome);
            case SANDSTONE:
                return new SandstoneBlock(newLoc, biome);
            case STONE:
                return new StoneBlock(newLoc, biome);
        }
        return null;
    }
}
