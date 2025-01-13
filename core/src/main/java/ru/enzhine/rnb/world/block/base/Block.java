package ru.enzhine.rnb.world.block.base;

import ru.enzhine.rnb.world.Collidable;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public interface Block extends Collidable {

    boolean isPenetrable();

    Location getLocation();

    BlockType getType();

    BiomeType getBiome();

    Material getMaterial();
}
