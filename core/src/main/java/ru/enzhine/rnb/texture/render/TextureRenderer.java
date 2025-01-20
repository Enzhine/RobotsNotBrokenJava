package ru.enzhine.rnb.texture.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.enzhine.rnb.texture.render.animated.AnimatedTextureRenderer;
import ru.enzhine.rnb.texture.render.stateful.StatefulTextureRenderer;
import ru.enzhine.rnb.texture.render.statical.StaticTextureRenderer;
import space.earlygrey.shapedrawer.ShapeDrawer;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StaticTextureRenderer.class, name = "static"),
        @JsonSubTypes.Type(value = AnimatedTextureRenderer.class, name = "animated"),
        @JsonSubTypes.Type(value = StatefulTextureRenderer.class, name = "stateful"),
        @JsonSubTypes.Type(value = RefTextureRenderer.class, name = "ref"),
})
public interface TextureRenderer<T extends RenderingContext> {
    void prepare();

    T newContext();

    Color getOutlineColor(T context);

    void render(T context, SpriteBatch spriteBatch, float x, float y, int srcX, int srcY, int width, int height);

    void renderLow(T context, SpriteBatch spriteBatch, ShapeDrawer shapeDrawer, float x, float y, int width, int height);
}
