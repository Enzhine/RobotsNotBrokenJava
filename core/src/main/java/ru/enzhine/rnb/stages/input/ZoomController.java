package ru.enzhine.rnb.stages.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;
import lombok.Setter;
import ru.enzhine.rnb.utils.MathUtils;

public class ZoomController {

    private final OrthographicCamera camera;
    private final float zoomScale;
    private final float secondsToReach;
    private float secondsAccumulation;
    private float virtualZoom;

    private float start;
    private float end;
    private float focusSeconds;
    private boolean isFocusing;

    private final float lowerBound = 0.03f;
    private final float upperBound = 10f;

    @Getter
    @Setter
    private boolean controlsEnabled = true;

    public ZoomController(OrthographicCamera camera, float zoomScale, float secondsToReach) {
        this.camera = camera;
        this.zoomScale = zoomScale;
        this.secondsToReach = secondsToReach;

        this.virtualZoom = camera.zoom;
    }

    public void onScroll(float yScale) {
        if (isFocusing) {
            return;
        }

        secondsAccumulation = 0f;
        virtualZoom += zoomScale * yScale;
        if (virtualZoom <= lowerBound) {
            virtualZoom = lowerBound;
        }
        if (virtualZoom >= upperBound) {
            virtualZoom = upperBound;
        }
    }

    public void zoomTo(float zoom, float focusSeconds) {
        isFocusing = true;
        this.focusSeconds = focusSeconds;

        secondsAccumulation = 0f;
        start = camera.zoom;
        end = zoom;
        if (end <= lowerBound) {
            end = lowerBound;
        }
        if (end >= upperBound) {
            end = upperBound;
        }
    }

    public void sync(float timeDelta) {
        if (isFocusing) {
            processFocusing(timeDelta);
        } else if (controlsEnabled) {
            processZoom(timeDelta);
        }
    }

    private void processZoom(float timeDelta) {
        secondsAccumulation += timeDelta;
        if (secondsAccumulation > secondsToReach) {
            secondsAccumulation = 0f;
        }
        camera.zoom = MathUtils.cerp(camera.zoom, virtualZoom, secondsAccumulation / secondsToReach);
    }

    private void processFocusing(float timeDelta) {
        secondsAccumulation += timeDelta;
        var progress = secondsAccumulation / focusSeconds;
        if (progress > 1f) {
            secondsAccumulation = 0f;
            isFocusing = false;
            virtualZoom = end;
        }
        camera.zoom = MathUtils.cerp(start, end, progress);
    }
}
