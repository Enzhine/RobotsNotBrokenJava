package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.Rendering;
import ru.enzhine.rnb.world.Location;

public abstract class StandartBlock implements Block, Rendering {

    private Location loc;
    private BlockType type;
    private BiomeType biomeType;

    private final Texture texture;
    public StandartBlock(String sprite, Location loc, BlockType bt, BiomeType biomeType) {
        this.loc = loc;
        this.type = bt;
        this.biomeType = biomeType;
        this.texture = Textures.getTexture(sprite);
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
    public void render(SpriteBatch batch, Viewport viewport) {
        batch.draw(texture, loc.getBlockX() * texture.getWidth(), loc.getBlockY() * texture.getHeight());
    }
}
