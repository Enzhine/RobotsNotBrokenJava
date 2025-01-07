package ru.enzhine.rnb.stages.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import ru.enzhine.rnb.utils.MathUtils;

public class ZoomController {

    private final OrthographicCamera camera;
    private final float zoomScale;
    private final float secondsToReach;
    private float secondsAccumulation;
    private float virtualZoom;

    public ZoomController(OrthographicCamera camera, float zoomScale, float secondsToReach) {
        this.camera = camera;
        this.zoomScale = zoomScale;
        this.secondsToReach = secondsToReach;

        this.virtualZoom = camera.zoom;
    }

    public void sync(float timeDelta) {
        secondsAccumulation += timeDelta;
        if (secondsAccumulation > secondsToReach) {
            secondsAccumulation = 0f;
        }
        camera.zoom = MathUtils.cerp(camera.zoom, virtualZoom, secondsAccumulation / secondsToReach);
    }

    public void onScroll(float yScale) {
        secondsAccumulation = 0f;
        virtualZoom += zoomScale * yScale;
    }
}
