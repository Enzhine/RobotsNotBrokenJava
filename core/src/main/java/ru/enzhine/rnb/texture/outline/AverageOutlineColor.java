package ru.enzhine.rnb.texture.outline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.utils.TextureUtils;

@Builder
@Jacksonized
public class AverageOutlineColor implements OutlineColorStrategy {

    @Builder.Default
    private final float tint = 0.65f;
    private final float tintAlpha = 1f;

    @Override
    public Color getColor(Texture texture) {
        return TextureUtils.getAverageColor(texture).mul(tint, tint, tint, tintAlpha);
    }
}
