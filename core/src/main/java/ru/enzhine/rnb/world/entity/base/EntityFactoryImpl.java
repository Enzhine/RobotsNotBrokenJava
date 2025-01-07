package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.entity.Robot;

public class EntityFactoryImpl implements EntityFactory {
    @Override
    public Entity makeEntity(EntityType entityType, double x, double y, Chunk c) {
        Location newLoc = new Location(x, y, c);
        return switch (entityType) {
            case ROBOT -> new Robot(newLoc);
        };
    }
}
