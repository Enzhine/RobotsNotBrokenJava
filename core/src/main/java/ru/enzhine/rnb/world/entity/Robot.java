package ru.enzhine.rnb.world.entity;

import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.InventoryHolder;
import ru.enzhine.rnb.world.entity.base.PhysicalEntity;
import ru.enzhine.rnb.world.item.base.ItemStack;
import ru.enzhine.rnb.world.robot.RobotController;

public interface Robot extends RobotController, Ticking, InventoryHolder, PhysicalEntity {

    ItemStack getHandItem();

    void pickHandItem(int idx);
}
