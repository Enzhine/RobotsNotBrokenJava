package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.Chunk;

public interface EntityFactory {
    Entity makeEntity(EntityType entityType, double x, double y, Chunk c);
}
