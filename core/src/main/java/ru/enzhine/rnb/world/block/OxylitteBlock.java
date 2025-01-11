package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.Material;

public class OxylitteBlock extends OpaqueBlock {
    public OxylitteBlock(Location loc, BiomeType biomeType) {
        super("block/oxylitte.json", loc, BlockType.OXYLITTE, Material.OXYLITTE, biomeType);
    }
}
