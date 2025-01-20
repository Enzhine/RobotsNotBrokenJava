package ru.enzhine.rnb.world.item.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.PlacedRendering;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.world.WorldImpl;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class BasicItemStack implements ItemStack, PlacedRendering {

    protected final ItemType itemType;
    protected final int maxCount;
    protected int currentCount;

    protected final TextureRenderer<RenderingContext> itemRenderer;
    protected final RenderingContext itemRendererContext;

    public BasicItemStack(TextureRenderer<RenderingContext> textureRenderer, ItemType itemType, int initialCount, int maxCount) {
        this.itemType = itemType;
        this.maxCount = maxCount;
        this.currentCount = initialCount;

        this.itemRenderer = textureRenderer;
        this.itemRendererContext = this.itemRenderer.newContext();
    }

    @Override
    public ItemType getType() {
        return itemType;
    }

    @Override
    public int getCount() {
        return currentCount;
    }

    @Override
    public void setCount(int count) {
        if(count < 1 || count > maxCount) {
            throw new RuntimeException("Itemstack count overflow");
        }
        currentCount = count;
    }

    @Override
    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public void renderAt(double gX, double gY, SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        itemRenderer.render(itemRendererContext, batch, (float) gX * WorldImpl.BLOCK_PIXEL_SIZE, (float) gY * WorldImpl.BLOCK_PIXEL_SIZE, 0, 0, WorldImpl.BLOCK_PIXEL_SIZE, WorldImpl.BLOCK_PIXEL_SIZE);
    }
}
