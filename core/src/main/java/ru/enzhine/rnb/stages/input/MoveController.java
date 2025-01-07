package ru.enzhine.rnb.stages.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;
import ru.enzhine.rnb.utils.MathUtils;

public class MoveController {

    private final Viewport viewport;
    private boolean isFocusing;
    private Vector3 start;
    private Vector3 end;
    private float focusSeconds;
    private float secondsAccumulation;

    private final Vector3 lastPos = new Vector3(0, 0, 0);

    @Getter
    @Setter
    private boolean controlsEnabled = true;

    public MoveController(Viewport viewport) {
        this.viewport = viewport;
        isFocusing = false;
    }

    public void focusAt(float wX, float wY, float focusSeconds) {
        start = new Vector3(viewport.getCamera().position);
        end = new Vector3(wX, wY, 0f);
        this.focusSeconds = focusSeconds;
        secondsAccumulation = 0f;
        isFocusing = true;
    }

    public void sync(float timeDelta) {
        if (isFocusing) {
            processFocusing(timeDelta);
        } else if (controlsEnabled) {
            processMove();
        }
    }

    private void processMove() {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            var scaleX = viewport.getWorldWidth() / viewport.getScreenWidth() * getZoom();
            var scaleY = viewport.getWorldHeight() / viewport.getScreenHeight() * getZoom();
            viewport.getCamera().translate((lastPos.x - Gdx.input.getX()) * scaleX, (Gdx.input.getY() - lastPos.y) * scaleY, 0);
        }
        lastPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    }

    private void processFocusing(float timeDelta) {
        secondsAccumulation += timeDelta;
        var progress = secondsAccumulation / focusSeconds;
        if (progress > 1f) {
            isFocusing = false;
            lastPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        } else {
            viewport.getCamera().position.set(MathUtils.cerp(start, end, progress));
        }
    }

    private float getZoom() {
        return ((OrthographicCamera) viewport.getCamera()).zoom;
    }
}
