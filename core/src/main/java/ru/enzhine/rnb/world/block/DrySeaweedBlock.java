package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class DrySeaweedBlock extends OpaqueBlock {
    public DrySeaweedBlock(Location loc, BiomeType biomeType) {
        super("block/dry_seaweed.png", loc, BlockType.DRY_SEAWEED, Material.DRY_SEAWEED, biomeType);
    }
}
