package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;
import ru.enzhine.rnb.world.WorldImpl;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class TransparentBlock extends OpaqueBlock {

    public static final float BG_SPEED = 0.1f;

    protected final TextureRenderer<RenderingContext> bgRenderer;
    protected final RenderingContext bgRendererContext;

    public TransparentBlock(String sprite, String spriteBG, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(TextureRenderers.getTextureRenderer(sprite), TextureRenderers.getTextureRenderer(spriteBG), loc, bt, material, biomeType);
    }

    public TransparentBlock(TextureRenderer<RenderingContext> textureRenderer, TextureRenderer<RenderingContext> textureBGRenderer, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        super(textureRenderer, loc, bt, material, biomeType);
        this.bgRenderer = textureBGRenderer;
        this.bgRendererContext = this.bgRenderer.newContext();
    }

    public boolean shouldRenderBG() {
        return true;
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        if (shouldRenderBG()) {
            float x = loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE;
            float y = loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE;
            int offsetX = (int) (viewport.getCamera().position.x * BG_SPEED);
            int offsetY = (int) (viewport.getCamera().position.y * BG_SPEED);
            int srcX = loc.getBlockX().intValue() * WorldImpl.BLOCK_PIXEL_SIZE + offsetX;
            int srcY = -loc.getBlockY().intValue() * WorldImpl.BLOCK_PIXEL_SIZE - offsetY;
            bgRenderer.render(bgRendererContext, batch, x, y, srcX, srcY, WorldImpl.BLOCK_PIXEL_SIZE, WorldImpl.BLOCK_PIXEL_SIZE);
        }

        super.render(batch, drawer, viewport);
    }
}
