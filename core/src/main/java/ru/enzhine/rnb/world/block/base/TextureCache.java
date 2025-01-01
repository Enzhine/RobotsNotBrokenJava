package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;

@Data
public class TextureCache {
    private final Texture originTexture;
    private final Color outlineColor;
}
