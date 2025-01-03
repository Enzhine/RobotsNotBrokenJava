package ru.enzhine.rnb.world.block.base;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public interface Block {

    boolean isPenetrable();

    Location getLocation();

    BlockType getType();

    BiomeType getBiome();

    Material getMaterial();
}
