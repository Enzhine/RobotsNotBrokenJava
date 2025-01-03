package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class StoneBlock extends OpaqueBlock {
    public StoneBlock(Location loc, BiomeType biomeType) {
        super("block/stone.png", loc, BlockType.STONE, Material.STONE, biomeType);
    }
}
