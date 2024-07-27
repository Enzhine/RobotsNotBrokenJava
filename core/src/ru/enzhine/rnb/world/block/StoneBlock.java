package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;

public class StoneBlock extends StandartBlock {
    public StoneBlock(Location loc, BiomeType biomeType) {
        super("blocks/stone.png", loc, BlockType.STONE, biomeType);
    }
}
