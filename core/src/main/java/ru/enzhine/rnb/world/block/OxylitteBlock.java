package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.StandartBlock;
import ru.enzhine.rnb.world.material.Material;

public class OxylitteBlock extends StandartBlock {
    public OxylitteBlock(Location loc, BiomeType biomeType) {
        super("blocks/oxylitte.png", loc, BlockType.OXYLITTE, Material.OXYLITTE, biomeType);
    }
}
