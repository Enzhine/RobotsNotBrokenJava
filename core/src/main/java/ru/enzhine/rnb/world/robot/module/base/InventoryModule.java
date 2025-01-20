package ru.enzhine.rnb.world.robot.module.base;

import ru.enzhine.rnb.world.item.base.ItemStack;

public interface InventoryModule {
    int getSize();

    ItemStack getAt(int idx);
}
