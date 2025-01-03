package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class StoneCoalOreBlock extends StandartBlock {
    public StoneCoalOreBlock(Location loc, BiomeType biomeType) {
        super("blocks/ores/stone_coal.json", loc, BlockType.STONE_COAL_ORE, Material.STONE, biomeType);
    }
}
