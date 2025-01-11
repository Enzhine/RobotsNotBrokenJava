package ru.enzhine.rnb.world.entity;

import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.entity.base.BasicEntity;
import ru.enzhine.rnb.world.entity.base.BoundingBox;
import ru.enzhine.rnb.world.entity.base.EntityType;

public class Robot extends BasicEntity {

    public Robot(Location location) {
        super(TextureRenderers.getTextureRenderer("entity/robot.json"), EntityType.ROBOT, location, new BoundingBox(-5d / WorldImpl.BLOCK_PIXEL_SIZE, 0d, 11, 11));
    }

    public void move() {
        setLocation(this.loc.translated(1, 0));
    }
}
