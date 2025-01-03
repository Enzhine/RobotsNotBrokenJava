package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class StoneCopperOreBlock extends StandartBlock {
    public StoneCopperOreBlock(Location loc, BiomeType biomeType) {
        super("blocks/ores/stone_copper.json", loc, BlockType.STONE_COAL_ORE, Material.STONE, biomeType);
    }
}
