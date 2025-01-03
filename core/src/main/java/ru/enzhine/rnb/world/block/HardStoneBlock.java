package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class HardStoneBlock extends StandartBlock {
    public HardStoneBlock(Location loc, BiomeType biomeType) {
        super("blocks/hard_stone.png", loc, BlockType.HARD_STONE, Material.HARD_STONE, biomeType);
    }
}
