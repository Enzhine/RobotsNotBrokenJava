package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class RichSoilBlock extends OpaqueBlock {
    public RichSoilBlock(Location loc, BiomeType biomeType) {
        super("block/rich_soil.png", loc, BlockType.RICH_SOIL, Material.RICH_SOIL, biomeType);
    }
}
