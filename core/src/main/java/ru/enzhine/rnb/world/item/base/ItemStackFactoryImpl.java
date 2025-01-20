package ru.enzhine.rnb.world.item.base;

import ru.enzhine.rnb.texture.TextureRenderers;

import java.util.Random;

public class ItemStackFactoryImpl implements ItemStackFactory {
    @Override
    public ItemStack makeItemstack(ItemType itemType, int initialCount, Random r) {
        switch (itemType) {
            case DRILL -> {
                return new BasicItemStack(TextureRenderers.getItemTextureRenderer(ItemType.SEAWEED_PIECE), ItemType.DRILL, 1, 1);
            }
        }

        int maxCount = switch (itemType) {
            case SOIL_PIECE, SAND_PIECE, RICH_SOIL_PIECE, SEAWEED_PIECE, DRY_SEAWEED_PIECE -> 4;
            case SOFT_STONE_SHARD, HARD_STONE_SHARD, STONE_SHARD, COPPER_ORE_INGOT, COAL_ORE_SHARD, OXYLITTE_SHARD -> 8;
            default -> 1;
        };
        return new RandomItemStack(TextureRenderers.getItemTextureRenderer(itemType), itemType, initialCount, maxCount, r, 4);
    }
}
