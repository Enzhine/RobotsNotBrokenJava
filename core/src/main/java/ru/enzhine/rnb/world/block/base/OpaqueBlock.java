package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.Rendering;
import ru.enzhine.rnb.texture.TextureCache;
import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public abstract class OpaqueBlock implements Block, Rendering {

    public static final int TEXTURE_WH = 16;

    protected final Location loc;
    protected final BlockType type;
    protected final BiomeType biomeType;
    protected final Material material;

    protected final Texture blockTexture;
    protected final Color outlineColor;

    public OpaqueBlock(String sprite, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(Textures.getTexture(sprite), loc, bt, material, biomeType);
    }

    public OpaqueBlock(TextureCache textureCache, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(textureCache.getOriginTexture(), textureCache.getOutlineColor(), loc, bt, material, biomeType);
    }

    public OpaqueBlock(Texture blockTexture, Color blockOutlineColor, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this.loc = loc;
        this.type = bt;
        this.biomeType = biomeType;
        this.material = material;
        this.blockTexture = blockTexture;
        this.outlineColor = blockOutlineColor;
    }

    @Override
    public boolean isPenetrable() {
        return false;
    }

    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public BiomeType getBiome() {
        return biomeType;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    private Block atBottom() {
        var bottom = this.loc.getChunk().get(this.loc.getChunkLocalX(), this.loc.getChunkLocalY() - 1);
        if (bottom == null) {
            bottom = this.loc.getWorld().getBlock(this.loc.getBlockX(), this.loc.getBlockY() - 1, false);
        }
        return bottom;
    }

    private Block atLeft() {
        var left = this.loc.getChunk().get(this.loc.getChunkLocalX() - 1, this.loc.getChunkLocalY());
        if (left == null) {
            left = this.loc.getWorld().getBlock(this.loc.getBlockX() - 1, this.loc.getBlockY(), false);
        }
        return left;
    }

    public boolean shouldRenderBlockTexture() {
        return true;
    }

    @Override
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        if (!shouldRenderBlockTexture()) {
            return;
        }

        batch.draw(
                blockTexture,
                loc.getBlockX() * TEXTURE_WH,
                loc.getBlockY() * TEXTURE_WH,
                loc.getBlockX().intValue() * TEXTURE_WH,
                -loc.getBlockY().intValue() * TEXTURE_WH,
                TEXTURE_WH,
                TEXTURE_WH);
    }

    public boolean shouldRenderOutline() {
        return true;
    }

    @Override
    public void shapeRender(ShapeRenderer renderer, Viewport viewport) {
        if (!shouldRenderOutline()) {
            return;
        }

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(outlineColor);
        var bottom = atBottom();
        if (bottom != null && bottom.getMaterial() != getMaterial()) {
            renderer.rectLine(
                    this.loc.getBlockX() * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    (this.loc.getBlockX() + 1) * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    2f
            );
        }
        var left = atLeft();
        if (left != null && left.getMaterial() != getMaterial()) {
            renderer.rectLine(
                    this.loc.getBlockX() * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    this.loc.getBlockX() * TEXTURE_WH,
                    (this.loc.getBlockY() + 1) * TEXTURE_WH,
                    2f
            );
        }
        renderer.end();
    }
}
