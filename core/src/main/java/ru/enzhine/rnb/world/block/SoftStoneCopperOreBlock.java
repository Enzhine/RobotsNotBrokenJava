package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class SoftStoneCopperOreBlock extends OpaqueBlock {
    public SoftStoneCopperOreBlock(Location loc, BiomeType biomeType) {
        super("block/ore/soft_stone_copper.json", loc, BlockType.SOFT_STONE_COPPER_ORE, Material.SOFT_STONE, biomeType);
    }
}
