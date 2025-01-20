package ru.enzhine.rnb.texture.render.stateful;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.texture.exception.TextureRendererException;
import ru.enzhine.rnb.texture.render.RefTextureRenderer;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Jacksonized
public class StatefulTextureRenderer implements TextureRenderer<StatefulRenderingContext> {

    @NonNull
    private final Map<String, TextureRenderer<RenderingContext>> states;
    @NonNull
    private final Integer beginState;

    private List<TextureRenderer<RenderingContext>> preparedStates;

    @Override
    public void prepare() {
        if (preparedStates != null) {
            throw new TextureRendererException("Already prepared");
        }

        int size = states.keySet().size();
        preparedStates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            preparedStates.add(null);
        }

        for (var entry : states.entrySet()) {
            var state = Integer.parseInt(entry.getKey());
            var value = entry.getValue();
            if (state < 0 || state >= size) {
                throw new TextureRendererException(String.format("Keys must be non-negative integers values from 0 to %d", size));
            }

            if (entry.getValue() instanceof RefTextureRenderer) {
                var path = ((RefTextureRenderer) entry.getValue()).getPath();
                preparedStates.set(state, TextureRenderers.getTextureRenderer(path));
            } else {
                preparedStates.set(state, value);
                value.prepare();
            }
        }
    }

    @Override
    public StatefulRenderingContext newContext() {
        return new StatefulRenderingContext(beginState, preparedStates.stream().map(TextureRenderer::newContext).toList());
    }

    @Override
    public Color getOutlineColor(StatefulRenderingContext context) {
        return getCurrentTextureRenderer(context).getOutlineColor(context.getCurrentContext());
    }

    @Override
    public void render(StatefulRenderingContext context, SpriteBatch spriteBatch, float x, float y, int srcX, int srcY, int width, int height) {
        getCurrentTextureRenderer(context).render(context.getCurrentContext(), spriteBatch, x, y, srcX, srcY, width, height);
    }

    @Override
    public void renderLow(StatefulRenderingContext context, SpriteBatch spriteBatch, ShapeDrawer shapeDrawer, float x, float y, int width, int height) {
        getCurrentTextureRenderer(context).renderLow(context.getCurrentContext(), spriteBatch, shapeDrawer, x, y, width, height);
    }

    private TextureRenderer<RenderingContext> getCurrentTextureRenderer(StatefulRenderingContext context) {
        return preparedStates.get(context.getCurrentState());
    }
}
