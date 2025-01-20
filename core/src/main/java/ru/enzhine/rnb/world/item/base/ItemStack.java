package ru.enzhine.rnb.world.item.base;

public interface ItemStack {
    ItemType getType();

    int getCount();

    void setCount(int count);

    int getMaxCount();
}
