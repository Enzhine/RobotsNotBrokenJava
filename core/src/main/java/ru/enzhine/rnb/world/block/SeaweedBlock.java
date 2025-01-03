package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class SeaweedBlock extends StandartBlock {
    public SeaweedBlock(Location loc, BiomeType biomeType) {
        super("blocks/seaweed.png", loc, BlockType.SEAWEED, Material.SEAWEED, biomeType);
    }
}
