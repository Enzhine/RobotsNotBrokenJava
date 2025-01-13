package ru.enzhine.rnb.world;

import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.Entity;
import ru.enzhine.rnb.world.block.base.Block;

import java.util.ListIterator;

public interface Chunk extends Ticking {

    byte MAX_LIGHT_LEVEL = (byte) 127;

    World getWorld();

    boolean isLoaded();

    Long getOffsetX();

    Long getOffsetY();

    Long getGlobalX();

    Long getGlobalY();

    void set(Block b);

    Block get(int localX, int localY);

    byte getLightLevel(int localX, int localY);

    void addEntity(Entity e);

    boolean removeEntity(Entity e);

    ListIterator<Entity> getEntities();

    boolean contains(double gX, double gY);
}
