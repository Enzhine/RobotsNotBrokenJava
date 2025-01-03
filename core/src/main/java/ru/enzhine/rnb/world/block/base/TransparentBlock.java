package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.TextureCache;
import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public abstract class TransparentBlock extends OpaqueBlock {

    public static final float BG_SPEED = 0.1f;

    protected final Texture textureBG;

    public TransparentBlock(String sprite, String spriteBG, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(Textures.getTexture(sprite), Textures.getTexture(spriteBG), loc, bt, material, biomeType);
    }

    public TransparentBlock(TextureCache textureCache, TextureCache textureBGCache, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(textureCache.getOriginTexture(), textureBGCache.getOriginTexture(), textureCache.getOutlineColor(), loc, bt, material, biomeType);
    }

    public TransparentBlock(Texture blockTexture, Texture blockBGTexture, Color blockOutlineColor, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        super(blockTexture, blockOutlineColor, loc, bt, material, biomeType);
        this.textureBG = blockBGTexture;
    }

    public boolean shouldRenderBG() {
        return true;
    }

    @Override
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        if (shouldRenderBG()) {
            int offsetX = (int) (viewport.getCamera().position.x * BG_SPEED);
            int offsetY = (int) (viewport.getCamera().position.y * BG_SPEED);

            batch.draw(
                    textureBG,
                    loc.getBlockX() * TEXTURE_WH,
                    loc.getBlockY() * TEXTURE_WH,
                    loc.getBlockX().intValue() * TEXTURE_WH + offsetX,
                    -loc.getBlockY().intValue() * TEXTURE_WH - offsetY,
                    TEXTURE_WH,
                    TEXTURE_WH);
        }

        super.batchRender(batch, viewport);
    }
}
