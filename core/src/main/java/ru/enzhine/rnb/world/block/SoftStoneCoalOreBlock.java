package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class SoftStoneCoalOreBlock extends StandartBlock {
    public SoftStoneCoalOreBlock(Location loc, BiomeType biomeType) {
        super("blocks/ores/soft_stone_coal.json", loc, BlockType.SOFT_STONE_COAL_ORE, Material.SOFT_STONE, biomeType);
    }
}
