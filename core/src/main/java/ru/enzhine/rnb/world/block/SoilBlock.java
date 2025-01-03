package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class SoilBlock extends StandartBlock {
    public SoilBlock(Location loc, BiomeType biomeType) {
        super("blocks/soil.png", loc, BlockType.SOIL, Material.SOIL, biomeType);
    }
}
