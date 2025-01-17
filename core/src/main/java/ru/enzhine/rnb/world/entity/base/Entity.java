package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.render.Rendering;

public interface Entity extends Rendering {

    Location getLocation();

    void setLocation(Location loc);

    EntityType getType();
}
