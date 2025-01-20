package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.item.base.ItemStack;

public interface Inventory {
    int getSize();

    ItemStack getAt(int idx);

    void setAt(int idx, ItemStack itemStack);
}
