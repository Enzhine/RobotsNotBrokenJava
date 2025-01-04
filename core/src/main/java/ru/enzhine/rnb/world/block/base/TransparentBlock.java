package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public abstract class TransparentBlock extends OpaqueBlock {

    public static final float BG_SPEED = 0.1f;

    protected final TextureRenderer bgRenderer;
    protected final RenderingContext bgRendererContext;

    public TransparentBlock(String sprite, String spriteBG, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(Textures.getTextureRenderer(sprite), Textures.getTextureRenderer(spriteBG), loc, bt, material, biomeType);
    }

    public TransparentBlock(TextureRenderer textureRenderer, TextureRenderer textureBGRenderer, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        super(textureRenderer, loc, bt, material, biomeType);
        this.bgRenderer = textureBGRenderer;
        this.bgRendererContext = this.bgRenderer.newContext();
    }

    public boolean shouldRenderBG() {
        return true;
    }

    @Override
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        if (shouldRenderBG()) {
            float x = loc.getBlockX() * TEXTURE_WH;
            float y = loc.getBlockY() * TEXTURE_WH;
            int offsetX = (int) (viewport.getCamera().position.x * BG_SPEED);
            int offsetY = (int) (viewport.getCamera().position.y * BG_SPEED);
            int srcX = loc.getBlockX().intValue() * TEXTURE_WH + offsetX;
            int srcY = -loc.getBlockY().intValue() * TEXTURE_WH - offsetY;
            bgRenderer.render(bgRendererContext, batch, x, y, srcX, srcY, TEXTURE_WH, TEXTURE_WH);
        }

        super.batchRender(batch, viewport);
    }
}
