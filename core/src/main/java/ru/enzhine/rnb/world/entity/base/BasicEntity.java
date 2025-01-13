package ru.enzhine.rnb.world.entity.base;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.stages.WorldStage;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.BoundingBox;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.Ticking;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class BasicEntity implements PhysicalEntity {

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

    @Override
    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    @Override
    public void appendVelocity(float dx, float dy) {
        velocity.add(dx, dy);
    }

    @Override
    public void onPhysicsUpdate(float deltaTime) {
        Block at = getLocation().getBlock();

        if (at.isPenetrable()) {
            processMovement(deltaTime);
        }
        setVelocity(Vector2.Zero);
    }

    private void processMovement(float deltaTime) {
        double dx = velocity.x * deltaTime;
        double dy = velocity.y * deltaTime;
        var newBB = getBoundingBox().translated(dx, dy);

        var bottomLeft = getAt(newBB.leftX(), newBB.bottomY());
        var topLeft = getAt(newBB.leftX(), newBB.topY());
        var topRight = getAt(newBB.rightX(), newBB.topY());
        var bottomRight = getAt(newBB.rightX(), newBB.bottomY());

        if (bottomLeft.isPenetrable() && topLeft.isPenetrable() &&
                topRight.isPenetrable() && bottomRight.isPenetrable()) {
            var newLoc = getLocation().translated(dx, dy);

            if (newLoc.getChunk() != getLocation().getChunk()) {
                getLocation().getChunk().removeEntity(this);
                newLoc.getChunk().addEntity(this);
            }
            setLocation(newLoc);
        }
    }

    private Block getAt(double x, double y) {
        if (loc.getChunk().contains(x, y)) {
            return new Location(x, y, loc.getChunk()).getBlock();
        } else {
            return loc.getWorld().getBlock(MathUtils.blockPos(x), MathUtils.blockPos(y), true);
        }
    }
}
