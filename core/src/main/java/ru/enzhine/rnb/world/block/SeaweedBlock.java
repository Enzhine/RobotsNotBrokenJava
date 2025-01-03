package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class SeaweedBlock extends OpaqueBlock {
    public SeaweedBlock(Location loc, BiomeType biomeType) {
        super("block/seaweed.png", loc, BlockType.SEAWEED, Material.SEAWEED, biomeType);
    }
}
