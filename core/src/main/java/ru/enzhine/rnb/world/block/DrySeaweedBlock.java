package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class DrySeaweedBlock extends StandartBlock {
    public DrySeaweedBlock(Location loc, BiomeType biomeType) {
        super("blocks/dry_seaweed.png", loc, BlockType.DRY_SEAWEED, Material.DRY_SEAWEED, biomeType);
    }
}
