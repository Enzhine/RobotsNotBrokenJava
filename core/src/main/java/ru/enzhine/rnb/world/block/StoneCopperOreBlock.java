package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class StoneCopperOreBlock extends OpaqueBlock {
    public StoneCopperOreBlock(Location loc, BiomeType biomeType) {
        super("block/ore/stone_copper.json", loc, BlockType.STONE_COAL_ORE, Material.STONE, biomeType);
    }
}
