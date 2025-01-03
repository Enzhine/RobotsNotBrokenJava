package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class SoilBlock extends OpaqueBlock {
    public SoilBlock(Location loc, BiomeType biomeType) {
        super("block/soil.png", loc, BlockType.SOIL, Material.SOIL, biomeType);
    }
}
