package ru.enzhine.rnb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.StandartBlock;

import static ru.enzhine.rnb.world.block.base.StandartBlock.TEXTURE_WH;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    ExtendViewport ev;
    WorldImpl w;

    @Override
    public void create() {
        w = new WorldImpl(10, 43, 0.2f, 0.2f);
        ev = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);
        long radius = 10;
        w.genRangeChunks(-(2 * radius), 2 * radius, -radius, radius);
//        w.genRangeChunks(-1L, 0L, -1L, 0L);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        ev.update(width, height);
    }

    private Vector3 getCurrentPos() {
        return ev.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    private void drawBlockInfo() {
        var proj = getCurrentPos();
        var font = new BitmapFont();
        var loc = new Location((double) proj.x / TEXTURE_WH, (double) proj.y / TEXTURE_WH, w.getChunk(0L, 0L, false));
        var chunk = w.getChunk(loc.getBlockX(), loc.getBlockY(), false);
        var block = (StandartBlock) w.getBlock(loc.getBlockX(), loc.getBlockY(), false);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        if (chunk != null) {
            shapeRenderer.rectLine(
                    chunk.getOffsetX() * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetY() + 1) * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetX() + 1) * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetY() + 1) * w.chunkSize() * TEXTURE_WH,
                    2f
            );
            shapeRenderer.rectLine(
                    chunk.getOffsetX() * w.chunkSize() * TEXTURE_WH,
                    chunk.getOffsetY() * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetX() + 1) * w.chunkSize() * TEXTURE_WH,
                    chunk.getOffsetY() * w.chunkSize() * TEXTURE_WH,
                    2f
            );
            shapeRenderer.rectLine(
                    chunk.getOffsetX() * w.chunkSize() * TEXTURE_WH,
                    chunk.getOffsetY() * w.chunkSize() * TEXTURE_WH,
                    chunk.getOffsetX() * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetY() + 1) * w.chunkSize() * TEXTURE_WH,
                    2f
            );
            shapeRenderer.rectLine(
                    (chunk.getOffsetX() + 1) * w.chunkSize() * TEXTURE_WH,
                    chunk.getOffsetY() * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetX() + 1) * w.chunkSize() * TEXTURE_WH,
                    (chunk.getOffsetY() + 1) * w.chunkSize() * TEXTURE_WH,
                    2f
            );
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.line(
                loc.getBlockX() * TEXTURE_WH,
                (loc.getBlockY() + 1) * TEXTURE_WH,
                (loc.getBlockX() + 1) * TEXTURE_WH,
                (loc.getBlockY() + 1) * TEXTURE_WH
        );
        shapeRenderer.line(
                loc.getBlockX() * TEXTURE_WH,
                loc.getBlockY() * TEXTURE_WH,
                (loc.getBlockX() + 1) * TEXTURE_WH,
                loc.getBlockY() * TEXTURE_WH
        );
        shapeRenderer.line(
                loc.getBlockX() * TEXTURE_WH,
                loc.getBlockY() * TEXTURE_WH,
                loc.getBlockX() * TEXTURE_WH,
                (loc.getBlockY() + 1) * TEXTURE_WH
        );
        shapeRenderer.line(
                (loc.getBlockX() + 1) * TEXTURE_WH,
                loc.getBlockY() * TEXTURE_WH,
                (loc.getBlockX() + 1) * TEXTURE_WH,
                (loc.getBlockY() + 1) * TEXTURE_WH
        );
        shapeRenderer.end();
        batch.begin();
        if (chunk != null) {
            font.draw(
                    batch,
                    String.format("G(%d;%d) L(%d;%d)", loc.getBlockX(), loc.getBlockY(), loc.getChunkLocalX(), loc.getChunkLocalY()),
                    loc.getBlockX() * TEXTURE_WH,
                    (loc.getBlockY() - 1) * TEXTURE_WH
            );
            font.draw(
                    batch,
                    block.getType().toString(),
                    loc.getBlockX() * TEXTURE_WH,
                    loc.getBlockY() * TEXTURE_WH
            );
            font.draw(
                    batch,
                    String.format("C(%d;%d) "+block.getBiome(), chunk.getOffsetX(), chunk.getOffsetY()),
                    loc.getBlockX() * TEXTURE_WH,
                    (loc.getBlockY() - 2) * TEXTURE_WH
            );
        }
        batch.end();
    }

    private void drawFPS() {
        var font = new BitmapFont();
        batch.begin();
        var proj = ev.getCamera().unproject(new Vector3(1, 1, 0));
        font.draw(batch, "FPS " + Gdx.graphics.getFramesPerSecond(), proj.x, proj.y);
        batch.end();
    }

    private final Vector3 lastPos = new Vector3(0, 0, 0);

    private void tryMove() {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            var scaleX = ev.getWorldWidth() / ev.getScreenWidth();
            var scaleY = ev.getWorldHeight() / ev.getScreenHeight();
            ev.getCamera().translate((lastPos.x - Gdx.input.getX()) * scaleX, (Gdx.input.getY() - lastPos.y) * scaleY, 0);
        }
        lastPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    }

    @Override
    public void render() {
        tryMove();
        ScreenUtils.clear(0, 0, 0, 1);
        ev.apply();
        batch.setProjectionMatrix(ev.getCamera().combined);
        batch.begin();
        w.batchRender(batch, ev);
        batch.end();
        shapeRenderer.setProjectionMatrix(ev.getCamera().combined);
        w.shapeRender(shapeRenderer, ev);
        drawBlockInfo();
        drawFPS();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
