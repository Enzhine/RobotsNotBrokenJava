package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class CopperClusterBlock extends StandartBlock {
    public CopperClusterBlock(Location loc, BiomeType biomeType) {
        super("blocks/copper_cluster.png", loc, BlockType.COPPER_CLUSTER, Material.COPPER_CLUSTER, biomeType);
    }
}
