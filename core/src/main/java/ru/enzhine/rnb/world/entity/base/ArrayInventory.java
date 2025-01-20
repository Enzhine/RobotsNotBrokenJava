package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.item.base.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayInventory implements Inventory {

    private final List<ItemStack> inv;

    public ArrayInventory(int size) {
        assert size > 0;

        var list = new ArrayList<ItemStack>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        this.inv = Collections.synchronizedList(list);
    }

    @Override
    public int getSize() {
        return inv.size();
    }

    @Override
    public ItemStack getAt(int idx) {
        return inv.get(idx);
    }

    @Override
    public void setAt(int idx, ItemStack itemStack) {
        inv.set(idx, itemStack);
    }
}
