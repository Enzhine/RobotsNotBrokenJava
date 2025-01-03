package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class SandBlock extends OpaqueBlock {
    public SandBlock(Location loc, BiomeType biomeType) {
        super("block/sand.png", loc, BlockType.SAND, Material.SAND, biomeType);
    }
}
