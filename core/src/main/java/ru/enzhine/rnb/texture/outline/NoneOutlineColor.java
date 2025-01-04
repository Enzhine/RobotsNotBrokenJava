package ru.enzhine.rnb.texture.outline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.utils.TextureUtils;

public class NoneOutlineColor implements OutlineColorStrategy {
    @Override
    public Color getColor(Texture texture) {
        return null;
    }
}
