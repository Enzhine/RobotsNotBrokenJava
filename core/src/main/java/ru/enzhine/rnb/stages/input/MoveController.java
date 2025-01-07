package ru.enzhine.rnb.stages.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MoveController {

    private final Viewport viewport;

    public MoveController(Viewport viewport) {
        this.viewport = viewport;
    }

    private final Vector3 lastPos = new Vector3(0, 0, 0);

    private float getZoom() {
        return ((OrthographicCamera) viewport.getCamera()).zoom;
    }

    public void update() {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            var scaleX = viewport.getWorldWidth() / viewport.getScreenWidth() * getZoom();
            var scaleY = viewport.getWorldHeight() / viewport.getScreenHeight() * getZoom();
            viewport.getCamera().translate((lastPos.x - Gdx.input.getX()) * scaleX, (Gdx.input.getY() - lastPos.y) * scaleY, 0);
        }
        lastPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    }
}
