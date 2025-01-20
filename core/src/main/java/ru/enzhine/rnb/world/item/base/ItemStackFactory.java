package ru.enzhine.rnb.world.item.base;

import java.util.Random;

public interface ItemStackFactory {
    ItemStack makeItemstack(ItemType itemType, int initialCount, Random r);
}
