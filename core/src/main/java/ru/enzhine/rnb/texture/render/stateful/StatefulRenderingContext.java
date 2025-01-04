package ru.enzhine.rnb.texture.render.stateful;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.enzhine.rnb.texture.render.RenderingContext;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatefulRenderingContext implements RenderingContext {

    private int currentState;
    private final List<RenderingContext> innerContexts;

    public RenderingContext getCurrentContext() {
        return innerContexts.get(currentState);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
