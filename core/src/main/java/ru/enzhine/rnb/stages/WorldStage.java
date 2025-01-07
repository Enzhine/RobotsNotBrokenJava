package ru.enzhine.rnb.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ru.enzhine.rnb.stages.input.BlockController;
import ru.enzhine.rnb.stages.input.MoveController;
import ru.enzhine.rnb.stages.input.ZoomController;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.entity.Robot;
import ru.enzhine.rnb.world.entity.base.Entity;
import ru.enzhine.rnb.world.entity.base.EntityType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class WorldStage extends Stage {

    private final SpriteBatch batch;
    private final ShapeDrawer shapeDrawer;
    private final Texture shapeDrawerTexture;

    private final ExtendViewport worldViewport;
    private final ExtendViewport hudViewport;

    private final ZoomController zoomController;
    private final MoveController moveController;
    private final BlockController blockController;

    private final WorldImpl w;

    public WorldStage(SpriteBatch batch) {
        this.batch = batch;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        shapeDrawerTexture = new Texture(pixmap);
        shapeDrawer = new ShapeDrawer(this.batch, new TextureRegion(shapeDrawerTexture, 0, 0, 1, 1));
        pixmap.dispose();

        worldViewport = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);
        hudViewport = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);

        zoomController = new ZoomController((OrthographicCamera) worldViewport.getCamera(), 0.06f, 1f);
        moveController = new MoveController(worldViewport);

        w = new WorldImpl(10, 43, 0.2f, 0.2f);
        initWorld();

        blockController = new BlockController(worldViewport, w);
    }

    private void initWorld() {
        long radius = 3;
        w.genRangeChunks(-2 * radius, 2 * radius, -2 * radius, 2 * radius);
//        w.genRangeChunks(0L, 0L, 0L, 0L);

        w.setBlock(BlockType.AIR, 4L, 4L);
        w.setBlock(BlockType.AIR, 5L, 4L);
        w.setBlock(BlockType.AIR, 4L, 5L);
        w.setBlock(BlockType.AIR, 5L, 5L);
        var robot = w.summonEntity(EntityType.ROBOT, 5d, 4d);

        focus(robot.getLocation().getX().floatValue() * WorldImpl.BLOCK_PIXEL_SIZE, robot.getLocation().getY().floatValue() * WorldImpl.BLOCK_PIXEL_SIZE, 0.5f, 2f);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        zoomController.onScroll(amountY);
        return true;
    }

    public void resize(int width, int height) {
        worldViewport.update(width, height);
        hudViewport.update(width, height, true);
    }

    public void update() {
        float td = Gdx.graphics.getDeltaTime();
        moveController.sync(td);
        blockController.update();
    }

    @Override
    public void draw() {
        zoomController.sync(Gdx.graphics.getDeltaTime());
        worldViewport.apply();
        batch.setProjectionMatrix(worldViewport.getCamera().combined);

        batch.begin();
        w.batch(batch, shapeDrawer, worldViewport);
        blockController.outlineSelected(shapeDrawer);
        batch.end();

        hudViewport.apply();
        drawFPS();

        super.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeDrawerTexture.dispose();

        super.dispose();
    }

    private void drawFPS() {
        BitmapFont font = new BitmapFont();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();
        font.draw(batch, String.format("FPS %d", Gdx.graphics.getFramesPerSecond()), 0, hudViewport.getCamera().viewportHeight);
        batch.end();
        font.dispose();
    }

    public void focus(float wX, float wY, float zoom, float seconds) {
        moveController.focusAt(wX, wY, seconds);
        zoomController.zoomTo(zoom, seconds);
    }
}
