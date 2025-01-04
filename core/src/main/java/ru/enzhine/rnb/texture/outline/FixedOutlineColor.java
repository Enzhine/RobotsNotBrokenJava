package ru.enzhine.rnb.texture.outline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class FixedOutlineColor implements OutlineColorStrategy {

    @NonNull
    private final String hex;

    @Override
    public Color getColor(Texture texture) {
        return Color.valueOf(hex);
    }
}
