package ru.enzhine.rnb.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import ru.enzhine.rnb.texture.processor.TextureProcessor;

@Data
public class TextureCache {
    private final TextureProcessor textureProcessor;
    private final Texture originTexture;
    private final Color outlineColor;
}
