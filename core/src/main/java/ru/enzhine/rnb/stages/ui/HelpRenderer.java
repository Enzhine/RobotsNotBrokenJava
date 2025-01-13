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
import ru.enzhine.rnb.stages.input.WorldUIController;
import ru.enzhine.rnb.world.block.base.Rendering;
import space.earlygrey.shapedrawer.ShapeDrawer;

@RequiredArgsConstructor
public class HelpRenderer implements Rendering, Disposable {

    private final RepeatingThreadExecutor serverThread;
    private final WorldUIController worldUIController;
    private final BitmapFont bitmapFont = new BitmapFont();

    @Getter
    @Setter
    private boolean showFps = false;

    @Getter
    @Setter
    private boolean showTps = false;

    @Getter
    @Setter
    private boolean showSelectionDetails = false;

    private float drawFps(SpriteBatch batch, float x, float y) {
        return y - bitmapFont.draw(batch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()), x, y).height;
    }

    private float drawTps(SpriteBatch batch, float x, float y) {
        y -= bitmapFont.draw(batch, String.format("TPS: %.3f", serverThread.getExecLastMillis() / 1_000f), x, y).height;
        return y - bitmapFont.draw(batch, String.format("TC: %d", serverThread.getExecCount()), x, y).height;
    }

    private float drawSelectionDetails(SpriteBatch batch, float x, float y) {
        if (worldUIController.getSelectedEntity() != null) {
            var entity = worldUIController.getSelectedEntity();

            y -= bitmapFont.draw(batch, String.format("TYPE: %s", entity.getType()), x, y).height;
            y -= bitmapFont.draw(batch, String.format("LOC: %.3f %.3f", entity.getLocation().getX(), entity.getLocation().getY()), x, y).height;
            return y - bitmapFont.draw(batch, String.format("VEL: %.3f %.3f", entity.getVelocity().x, entity.getVelocity().y), x, y).height;
        } else if (worldUIController.getSelectedBlock() != null) {
            var block = worldUIController.getSelectedBlock();
        }
        return y;
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        float x = 0;
        float y = viewport.getCamera().viewportHeight;
        float lineGap = 5f;

        if (showFps) {
            y = drawFps(batch, x, y) - lineGap;
        }
        if (showTps) {
            y = drawTps(batch, x, y) - lineGap;
        }
        if (showSelectionDetails) {
            y = drawSelectionDetails(batch, x, y) - lineGap;
        }
    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
    }
}
