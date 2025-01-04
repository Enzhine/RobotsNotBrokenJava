package ru.enzhine.rnb.texture;

import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;

public interface TextureRendererFactory {
    TextureRenderer<RenderingContext> makeRenderer(String rendererConfigPath);
}
