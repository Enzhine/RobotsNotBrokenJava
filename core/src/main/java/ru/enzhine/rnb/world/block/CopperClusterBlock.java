package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class CopperClusterBlock extends OpaqueBlock {
    public CopperClusterBlock(Location loc, BiomeType biomeType) {
        super("block/copper_cluster.png", loc, BlockType.COPPER_CLUSTER, Material.COPPER_CLUSTER, biomeType);
    }
}
