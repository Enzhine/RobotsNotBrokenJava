package ru.enzhine.rnb.world.entity;

import ru.enzhine.rnb.world.Location;

public interface Entity {

    Location getLocation();

    void setLocation(Location loc);

    EntityType getType();
}
