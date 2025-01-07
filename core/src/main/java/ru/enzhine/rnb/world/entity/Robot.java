package ru.enzhine.rnb.world.entity;

import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.OpaqueBlock;
import ru.enzhine.rnb.world.entity.base.BasicEntity;
import ru.enzhine.rnb.world.entity.base.BoundingBox;
import ru.enzhine.rnb.world.entity.base.EntityType;

public class Robot extends BasicEntity {

    public Robot(Location location) {
        super(Textures.getTextureRenderer("entity/basic_robot.png"), EntityType.ROBOT, location, new BoundingBox(-5.5d / WorldImpl.BLOCK_PIXEL_SIZE, 0d, 11, 11));
    }

    public void move() {
        setLocation(this.loc.translated(1, 0));
    }
}
