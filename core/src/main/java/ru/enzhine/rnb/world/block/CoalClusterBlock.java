package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class CoalClusterBlock extends OpaqueBlock {
    public CoalClusterBlock(Location loc, BiomeType biomeType) {
        super("block/coal_cluster.png", loc, BlockType.COAL_CLUSTER, Material.COAL_CLUSTER, biomeType);
    }
}
