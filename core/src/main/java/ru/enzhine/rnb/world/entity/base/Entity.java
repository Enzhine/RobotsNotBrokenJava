package ru.enzhine.rnb.world.entity.base;

import com.badlogic.gdx.math.Vector2;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.Rendering;

public interface Entity extends Collidable, Rendering {

    Location getLocation();

    Vector2 getVelocity();

    void setLocation(Location loc);

    EntityType getType();
}
