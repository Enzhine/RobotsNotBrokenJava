package ru.enzhine.rnb.texture.render.animated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.enzhine.rnb.texture.render.RenderingContext;

@Getter
@Setter
@AllArgsConstructor
public class AnimatedRenderingContext implements RenderingContext {

    private int currentFrame;
    private float accumulation;
    private final float frameThreshold;
    private boolean inwards;
    private boolean finished;
}
