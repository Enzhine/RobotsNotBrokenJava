package ru.enzhine.rnb.world.entity.base;

import com.badlogic.gdx.math.Vector2;
import ru.enzhine.rnb.world.Collidable;

public interface PhysicalEntity extends Entity, Collidable {

    Vector2 getVelocity();

    void setVelocity(Vector2 velocity);

    void appendVelocity(float dx, float dy);

    void onPhysicsUpdate(float deltaTime);
}
