package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;

public class HardStoneBlock extends StandartBlock {
    public HardStoneBlock(Location loc, BiomeType biomeType) {
        super("blocks/hard_stone.png", loc, BlockType.HARD_STONE, biomeType);
    }
}
