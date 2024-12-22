package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.StandartBlock;

public class DirtBlock extends StandartBlock {
    public DirtBlock(Location loc, BiomeType biomeType) {
        super("blocks/dirt.png", loc, BlockType.DIRT, biomeType);
    }
}
