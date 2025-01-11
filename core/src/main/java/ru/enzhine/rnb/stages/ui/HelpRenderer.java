package ru.enzhine.rnb.stages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.enzhine.rnb.server.RepeatingThreadExecutor;
import ru.enzhine.rnb.world.block.base.Rendering;
import space.earlygrey.shapedrawer.ShapeDrawer;

@RequiredArgsConstructor
public class HelpRenderer implements Rendering, Disposable {

    private final RepeatingThreadExecutor serverThread;
    private final BitmapFont bitmapFont = new BitmapFont();

    @Getter
    @Setter
    private boolean showFps = false;

    @Getter
    @Setter
    private boolean showTps = false;

    private GlyphLayout drawFps(SpriteBatch batch, float x, float y) {
        return bitmapFont.draw(batch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()), x, y);
    }

    private GlyphLayout drawTps(SpriteBatch batch, float x, float y) {
        var gl = bitmapFont.draw(batch, String.format("TPS: %.3f", serverThread.getExecLastMillis() / 1_000f), x, y);
        return bitmapFont.draw(batch, String.format("TC: %d", serverThread.getExecCount()), x, y - gl.height);
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        float x = 0;
        float y = viewport.getCamera().viewportHeight;
        float lineGap = 5f;

        if (showFps) {
            y -= (drawFps(batch, x, y).height + lineGap);
        }
        if (showTps) {
            y -= (drawTps(batch, x, y).height + lineGap);
        }
    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
    }
}
