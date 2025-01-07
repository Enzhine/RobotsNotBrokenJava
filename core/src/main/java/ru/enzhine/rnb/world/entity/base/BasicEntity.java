package ru.enzhine.rnb.world.entity.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class BasicEntity implements Entity {

    protected Location loc;
    protected final EntityType entityType;
    protected final BoundingBox boundingBox;

    protected final TextureRenderer<RenderingContext> renderer;
    protected RenderingContext renderingContext;

    public BasicEntity(TextureRenderer<RenderingContext> textureRenderer, EntityType entityType, Location location, BoundingBox boundingBox) {
        this.loc = location;
        this.entityType = entityType;
        this.boundingBox = boundingBox;
        this.renderer = textureRenderer;
        this.renderingContext = this.renderer.newContext();
    }

    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public void setLocation(Location loc) {
        this.loc = loc;
    }

    @Override
    public EntityType getType() {
        return this.entityType;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox.translated(this.loc.getX(), this.loc.getY());
    }

    @Override
    public void batch(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        var bb = getBoundingBox();
        renderer.render(this.renderingContext, batch, (int) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE), (int) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE), 0, 0, bb.getPxWidth(), bb.getPxHeight());
    }
}
