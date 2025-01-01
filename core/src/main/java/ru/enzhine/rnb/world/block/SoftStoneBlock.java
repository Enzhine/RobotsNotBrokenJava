package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;

public class SoftStoneBlock extends StandartBlock {
    public SoftStoneBlock(Location loc, BiomeType biomeType) {
        super("blocks/soft_stone.png", loc, BlockType.SOFT_STONE, biomeType);
    }
}
