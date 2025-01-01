package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.StandartBlock;

public class SandBlock extends StandartBlock {
    public SandBlock(Location loc, BiomeType biomeType) {
        super("blocks/sand.png", loc, BlockType.SAND, biomeType);
    }
}
