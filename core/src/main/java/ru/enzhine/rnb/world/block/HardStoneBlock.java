package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class HardStoneBlock extends OpaqueBlock {
    public HardStoneBlock(Location loc, BiomeType biomeType) {
        super("block/hard_stone.png", loc, BlockType.HARD_STONE, Material.HARD_STONE, biomeType);
    }
}
