package ru.enzhine.rnb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ru.enzhine.rnb.world.WorldImpl;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    ExtendViewport ev;
    WorldImpl w;

    @Override
    public void create() {
        w = new WorldImpl(10);
        ev = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);
        w.genRangeChunks(-3L, 3L, -3L, 3L);

        batch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        ev.update(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        ev.apply();
        batch.setProjectionMatrix(ev.getCamera().combined);
        batch.begin();
        w.render(batch, ev);
        batch.end();
        //System.out.println(Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
