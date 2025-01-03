package ru.enzhine.rnb.world.block.base;

import ru.enzhine.rnb.world.Fluid;

public interface Floodable {
    byte MAX_FLOOD_LEVEL = 16;

    byte getTotalLevel();

    byte getLevel(Fluid fluid);

    void setLevel(Fluid fluid, byte level);

    byte[] levels();

    boolean isStill();
}
