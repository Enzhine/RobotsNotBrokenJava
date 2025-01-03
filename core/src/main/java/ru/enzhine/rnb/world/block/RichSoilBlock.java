package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class RichSoilBlock extends StandartBlock {
    public RichSoilBlock(Location loc, BiomeType biomeType) {
        super("blocks/rich_soil.png", loc, BlockType.RICH_SOIL, Material.RICH_SOIL, biomeType);
    }
}
