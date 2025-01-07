package ru.enzhine.rnb.texture.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.texture.exception.TextureRendererException;
import ru.enzhine.rnb.texture.outline.AverageOutlineColor;
import ru.enzhine.rnb.texture.outline.OutlineColorStrategy;
import ru.enzhine.rnb.texture.preprocessor.TexturePreprocessor;
import ru.enzhine.rnb.utils.TextureUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

@Builder
@Jacksonized
public class StaticTextureRenderer implements TextureRenderer<RenderingContext> {

    @NonNull
    private final TexturePreprocessor src;
    @NonNull
    @Builder.Default
    private TextureRendererProperties properties = TextureRendererProperties.builder()
            .uWrap(Texture.TextureWrap.Repeat)
            .vWrap(Texture.TextureWrap.Repeat)
            .build();

    @NonNull
    @Builder.Default
    private OutlineColorStrategy outline = AverageOutlineColor.builder().build();

    private Texture preparedTexture;
    private Color preparedLowColor;
    private Color preparedOutlineColor;

    @Override
    public void prepare() {
        if (preparedTexture != null) {
            throw new TextureRendererException("Already prepared");
        }

        preparedTexture = src.process();
        preparedTexture.setWrap(properties.getUWrap(), properties.getVWrap());
        preparedLowColor = TextureUtils.getAverageColor(preparedTexture);
        preparedOutlineColor = outline.getColor(preparedTexture);
    }

    @Override
    public RenderingContext newContext() {
        return null;
    }

    @Override
    public Color getOutlineColor(RenderingContext context) {
        if (preparedOutlineColor == null) {
            throw new TextureRendererException("Outline color not supported");
        }
        return preparedOutlineColor;
    }

    @Override
    public void render(RenderingContext context, SpriteBatch spriteBatch, float x, float y, int srcX, int srcY, int width, int height) {
        spriteBatch.draw(preparedTexture, x, y, srcX, srcY, width, height);
    }

    @Override
    public void renderLow(RenderingContext context, SpriteBatch spriteBatch, ShapeDrawer shapeDrawer, float x, float y, int width, int height) {
        shapeDrawer.filledRectangle(x, y, width, height, preparedLowColor);
    }
}
