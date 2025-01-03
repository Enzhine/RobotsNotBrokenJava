package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class SoftStoneCopperOreBlock extends StandartBlock {
    public SoftStoneCopperOreBlock(Location loc, BiomeType biomeType) {
        super("blocks/ores/soft_stone_copper.json", loc, BlockType.SOFT_STONE_COPPER_ORE, Material.SOFT_STONE, biomeType);
    }
}
