package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class StoneBlock extends StandartBlock {
    public StoneBlock(Location loc, BiomeType biomeType) {
        super("blocks/stone.png", loc, BlockType.STONE, Material.STONE, biomeType);
    }
}
