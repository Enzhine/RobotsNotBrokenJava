package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class CoalClusterBlock extends StandartBlock {
    public CoalClusterBlock(Location loc, BiomeType biomeType) {
        super("blocks/coal_cluster.png", loc, BlockType.COAL_CLUSTER, Material.COAL_CLUSTER, biomeType);
    }
}
