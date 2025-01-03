package ru.enzhine.rnb.world.block.base;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.*;

public class BlockFactoryImpl implements BlockFactory {

    @Override
    public Block makeBlock(BlockType type, long x, long y, BiomeType biome, Chunk c) {
        Location newLoc = new Location((double)x, (double)y, c);
        return switch (type) {
            case SOIL -> new SoilBlock(newLoc, biome);
            case RICH_SOIL -> new RichSoilBlock(newLoc, biome);
            case SAND -> new SandBlock(newLoc, biome);
            case SOFT_STONE -> new SoftStoneBlock(newLoc, biome);
            case HARD_STONE -> new HardStoneBlock(newLoc, biome);
            case STONE -> new StoneBlock(newLoc, biome);
            case SOFT_STONE_COPPER_ORE -> new SoftStoneCopperOreBlock(newLoc, biome);
            case SOFT_STONE_COAL_ORE -> new SoftStoneCoalOreBlock(newLoc, biome);
            case STONE_COAL_ORE -> new StoneCoalOreBlock(newLoc, biome);
            case STONE_COPPER_ORE -> new StoneCopperOreBlock(newLoc, biome);
            case SEAWEED -> new SeaweedBlock(newLoc, biome);
            case DRY_SEAWEED -> new DrySeaweedBlock(newLoc, biome);
            case OXYLITTE -> new OxylitteBlock(newLoc, biome);
            case COAL_CLUSTER -> new CoalClusterBlock(newLoc, biome);
            case COPPER_CLUSTER -> new CopperClusterBlock(newLoc, biome);
        };
    }
}
