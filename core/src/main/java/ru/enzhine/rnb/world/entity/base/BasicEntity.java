package ru.enzhine.rnb.world.entity.base;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
    protected RenderingContext rendererContext;

    protected final Vector2 velocity;

    public BasicEntity(TextureRenderer<RenderingContext> textureRenderer, EntityType entityType, Location location, BoundingBox boundingBox) {
        this.loc = location;
        this.entityType = entityType;
        this.boundingBox = boundingBox;
        this.renderer = textureRenderer;
        this.rendererContext = this.renderer.newContext();
        this.velocity = new Vector2(0f, 0f);
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

    public boolean shouldRenderEntityTexture() {
        return true;
    }

    protected boolean needToOptimize(Viewport viewport) {
        var zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
        return zoom >= 2f;
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        if (!shouldRenderEntityTexture()) {
            return;
        }

        var bb = getBoundingBox();
        if (needToOptimize(viewport)) {
            renderer.renderLow(this.rendererContext, batch, drawer, (int) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE), (int) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE), bb.getPxWidth(), bb.getPxHeight());
            return;
        }

        renderer.render(this.rendererContext, batch, (int) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE), (int) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE), 0, 0, bb.getPxWidth(), bb.getPxHeight());
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }
}
