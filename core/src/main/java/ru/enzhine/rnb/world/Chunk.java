package ru.enzhine.rnb.world;

import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.Entity;
import ru.enzhine.rnb.world.block.base.Block;

import java.util.ListIterator;

public interface Chunk extends Ticking {

    Long getOffsetX();

    Long getGlobalX();

    Long getOffsetY();

    Long getGlobalY();

    void set(Block b);

    Block get(int localX, int localY);

    byte MAX_LIGHT_LEVEL = (byte) 127;

    byte getLightLevel(int localX, int localY);

    ListIterator<Entity> getEntities();

    boolean isLoaded();

    World getWorld();
}
