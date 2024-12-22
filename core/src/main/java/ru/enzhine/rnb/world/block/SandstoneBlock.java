package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.StandartBlock;

public class SandstoneBlock extends StandartBlock {
    public SandstoneBlock(Location loc, BiomeType biomeType) {
        super("blocks/sandstone.png", loc, BlockType.SANDSTONE, biomeType);
    }
}
