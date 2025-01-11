package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;
import ru.enzhine.rnb.world.WorldImpl;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class OpaqueBlock implements Block, Rendering {

    protected final Location loc;
    protected final BlockType type;
    protected final BiomeType biomeType;
    protected final Material material;

    protected final TextureRenderer<RenderingContext> renderer;
    protected RenderingContext rendererContext;

    public OpaqueBlock(String sprite, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(TextureRenderers.getTextureRenderer(sprite), loc, bt, material, biomeType);
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

    public boolean shouldRenderOutline() {
        return true;
    }

    protected boolean needToOptimize(Viewport viewport) {
        var zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
        return zoom >= 2f;
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        if (!shouldRenderBlockTexture()) {
            return;
        }

        float x = loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE;
        float y = loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE;

        if (needToOptimize(viewport)) {
            renderer.renderLow(this.rendererContext, batch, drawer, x, y, WorldImpl.BLOCK_PIXEL_SIZE, WorldImpl.BLOCK_PIXEL_SIZE);
            return;
        }

        int srcX = loc.getBlockX().intValue() * WorldImpl.BLOCK_PIXEL_SIZE;
        int srcY = -loc.getBlockY().intValue() * WorldImpl.BLOCK_PIXEL_SIZE;
        renderer.render(this.rendererContext, batch, x, y, srcX, srcY, WorldImpl.BLOCK_PIXEL_SIZE, WorldImpl.BLOCK_PIXEL_SIZE);

        var outlineColor = this.renderer.getOutlineColor(this.rendererContext);
        if (shouldRenderOutline() && outlineColor != null) {
            drawOutline(drawer, outlineColor);
        }
    }

    void drawOutline(ShapeDrawer drawer, Color outlineColor) {
        var bottom = atBottom();
        if (bottom != null && bottom.getMaterial() != getMaterial()) {
            drawer.line(
                    this.loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
                    this.loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
                    (this.loc.getBlockX() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
                    this.loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
                    outlineColor,
                    2f
            );
        }
        var left = atLeft();
        if (left != null && left.getMaterial() != getMaterial()) {
            drawer.line(
                    this.loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
                    this.loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
                    this.loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
                    (this.loc.getBlockY() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
                    outlineColor,
                    2f
            );
        }
    }
}
