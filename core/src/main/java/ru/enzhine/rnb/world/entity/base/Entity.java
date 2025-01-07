package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.Rendering;

public interface Entity extends Collidable, Rendering {

    Location getLocation();

    void setLocation(Location loc);

    EntityType getType();
}
