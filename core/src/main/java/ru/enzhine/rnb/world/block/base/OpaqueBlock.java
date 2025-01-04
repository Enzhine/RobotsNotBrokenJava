package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public abstract class OpaqueBlock implements Block, Rendering {

    public static final int TEXTURE_WH = 16;

    protected final Location loc;
    protected final BlockType type;
    protected final BiomeType biomeType;
    protected final Material material;

    protected final TextureRenderer<RenderingContext> renderer;
    protected final RenderingContext rendererContext;

    public OpaqueBlock(String sprite, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(Textures.getTextureRenderer(sprite), loc, bt, material, biomeType);
    }

    public OpaqueBlock(TextureRenderer<RenderingContext> textureRenderer, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this.loc = loc;
        this.type = bt;
        this.biomeType = biomeType;
        this.material = material;

        this.renderer = textureRenderer;
        this.rendererContext = this.renderer.newContext();
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

        float x = loc.getBlockX() * TEXTURE_WH;
        float y = loc.getBlockY() * TEXTURE_WH;
        int srcX = loc.getBlockX().intValue() * TEXTURE_WH;
        int srcY = -loc.getBlockY().intValue() * TEXTURE_WH;
        renderer.render(this.rendererContext, batch, x, y, srcX, srcY, TEXTURE_WH, TEXTURE_WH);
    }

    public boolean shouldRenderOutline() {
        return true;
    }

    @Override
    public void shapeRender(ShapeRenderer renderer, Viewport viewport) {
        if (!shouldRenderOutline()) {
            return;
        }

        var outlineColor = this.renderer.getOutlineColor(this.rendererContext);
        if (outlineColor == null) {
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
