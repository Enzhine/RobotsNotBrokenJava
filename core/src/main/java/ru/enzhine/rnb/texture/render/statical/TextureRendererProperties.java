package ru.enzhine.rnb.texture.render.statical;

import com.badlogic.gdx.graphics.Texture;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class TextureRendererProperties {
    private Texture.TextureWrap uWrap;
    private Texture.TextureWrap vWrap;
}
