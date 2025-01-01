package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.SpriteRendering;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.Location;

public abstract class StandartBlock implements Block, SpriteRendering {

    public static final int TEXTURE_WH = 16;

    private static boolean validateTexture(Texture texture) {
        return texture.getWidth() % TEXTURE_WH == 0
                && texture.getHeight() % TEXTURE_WH == 0;
    }

    private final Location loc;
    private final BlockType type;
    private final BiomeType biomeType;
    private final String spriteName;
    private final TextureRegion texture;
    private final Color outlineColor;

    public StandartBlock(String sprite, Location loc, BlockType bt, BiomeType biomeType) {
        this.loc = loc;
        this.type = bt;
        this.biomeType = biomeType;
        var cache = Textures.getTexture(sprite);
        if (!validateTexture(cache.getOriginTexture())) {
            throw new RuntimeException(String.format("Texture ratios is not divisible by %d", TEXTURE_WH));
        }
        this.spriteName = sprite;
        this.texture = getTextureRegion(cache.getOriginTexture(), this.loc);
        this.outlineColor = cache.getOutlineColor();
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

    private TextureRegion getTextureRegion(Texture texture, Location location) {
        int kW = texture.getWidth() / TEXTURE_WH;
        int kH = texture.getHeight() / TEXTURE_WH;

        int texX = MathUtils.remainder(location.getBlockX(), kW);
        int texY = MathUtils.remainder(location.getBlockY(), kH);
        return new TextureRegion(
                texture,
                texX * TEXTURE_WH,
                (kH - 1 - texY) * TEXTURE_WH,
                TEXTURE_WH,
                TEXTURE_WH
        );
    }

    public Block atTop() {
        var top = this.loc.getChunk().get(this.loc.getChunkLocalX(), this.loc.getChunkLocalY() + 1);
        if(top == null) {
            top = this.loc.getWorld().getBlock(this.loc.getBlockX(), this.loc.getBlockY() + 1, false);
        }
        return top;
    }

    public Block atBottom() {
        var bottom = this.loc.getChunk().get(this.loc.getChunkLocalX(), this.loc.getChunkLocalY() - 1);
        if(bottom == null) {
            bottom = this.loc.getWorld().getBlock(this.loc.getBlockX(), this.loc.getBlockY() - 1, false);
        }
        return bottom;
    }

    public Block atLeft() {
        var left = this.loc.getChunk().get(this.loc.getChunkLocalX() - 1, this.loc.getChunkLocalY());
        if(left == null) {
            left = this.loc.getWorld().getBlock(this.loc.getBlockX() - 1, this.loc.getBlockY(), false);
        }
        return left;
    }

    public Block atRight() {
        var right = this.loc.getChunk().get(this.loc.getChunkLocalX() + 1, this.loc.getChunkLocalY());
        if(right == null) {
            right = this.loc.getWorld().getBlock(this.loc.getBlockX() + 1, this.loc.getBlockY(), false);
        }
        return right;
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        batch.draw(texture, loc.getBlockX() * TEXTURE_WH, loc.getBlockY() * TEXTURE_WH);
    }

    @Override
    public void shapeRender(ShapeRenderer renderer, Viewport viewport) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(outlineColor);
        var top = atTop();
        if (top instanceof SpriteRendering && !((SpriteRendering) top).getSpriteName().equals(getSpriteName())) {
            renderer.rectLine(
                    this.loc.getBlockX() * TEXTURE_WH,
                    (this.loc.getBlockY() + 1) * TEXTURE_WH,
                    (this.loc.getBlockX() + 1) * TEXTURE_WH,
                    (this.loc.getBlockY() + 1) * TEXTURE_WH,
                    2f
            );
        }
        var bottom = atBottom();
        if (bottom instanceof SpriteRendering && !((SpriteRendering) bottom).getSpriteName().equals(getSpriteName())) {
            renderer.rectLine(
                    this.loc.getBlockX() * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    (this.loc.getBlockX() + 1) * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    2f
            );
        }
        var left = atLeft();
        if (left instanceof SpriteRendering && !((SpriteRendering) left).getSpriteName().equals(getSpriteName())) {
            renderer.rectLine(
                    this.loc.getBlockX() * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    this.loc.getBlockX() * TEXTURE_WH,
                    (this.loc.getBlockY() + 1) * TEXTURE_WH,
                    2f
            );
        }
        var right = atRight();
        if (right instanceof SpriteRendering && !((SpriteRendering) right).getSpriteName().equals(getSpriteName())) {
            renderer.rectLine(
                    (this.loc.getBlockX() + 1) * TEXTURE_WH,
                    this.loc.getBlockY() * TEXTURE_WH,
                    (this.loc.getBlockX() + 1) * TEXTURE_WH,
                    (this.loc.getBlockY() + 1) * TEXTURE_WH,
                    2f
            );
        }
        renderer.end();
    }
}
