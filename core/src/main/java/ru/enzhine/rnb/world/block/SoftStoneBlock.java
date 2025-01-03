package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class SoftStoneBlock extends OpaqueBlock {
    public SoftStoneBlock(Location loc, BiomeType biomeType) {
        super("block/soft_stone.png", loc, BlockType.SOFT_STONE, Material.SOFT_STONE, biomeType);
    }
}
