package ru.enzhine.rnb.texture.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.texture.exception.TextureRendererException;

@Getter
@Builder
@Jacksonized
public class RefTextureRenderer implements TextureRenderer<RenderingContext> {

    @NonNull
    private final String path;

    @Override
    public void prepare() {
        throw new TextureRendererException("This renderer must be replaced with real one");
    }

    @Override
    public RenderingContext newContext() {
        throw new TextureRendererException("This renderer must be replaced with real one");
    }

    @Override
    public Color getOutlineColor(RenderingContext context) {
        throw new TextureRendererException("This renderer must be replaced with real one");
    }

    @Override
    public void render(RenderingContext context, SpriteBatch spriteBatch, float x, float y, int srcX, int srcY, int width, int height) {
        throw new TextureRendererException("This renderer must be replaced with real one");
    }
}
