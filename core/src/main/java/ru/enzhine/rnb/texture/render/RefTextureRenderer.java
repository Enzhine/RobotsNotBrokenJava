package ru.enzhine.rnb.texture.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.texture.exception.TextureRendererException;
import space.earlygrey.shapedrawer.ShapeDrawer;

@Getter
@Builder
@Jacksonized
public class RefTextureRenderer implements TextureRenderer<RenderingContext> {

    @NonNull
    private final String path;

    @Override
    public void prepare() {
        throw new TextureRendererException("This renderer must be replaced with referenced one");
    }

    @Override
    public RenderingContext newContext() {
        throw new TextureRendererException("This renderer must be replaced with referenced one");
    }

    @Override
    public Color getOutlineColor(RenderingContext context) {
        throw new TextureRendererException("This renderer must be replaced with referenced one");
    }

    @Override
    public void render(RenderingContext context, SpriteBatch spriteBatch, float x, float y, int srcX, int srcY, int width, int height) {
        throw new TextureRendererException("This renderer must be replaced with referenced one");
    }

    @Override
    public void renderLow(RenderingContext context, SpriteBatch spriteBatch, ShapeDrawer shapeDrawer, float x, float y, int width, int height) {
        throw new TextureRendererException("This renderer must be replaced with referenced one");
    }
}
