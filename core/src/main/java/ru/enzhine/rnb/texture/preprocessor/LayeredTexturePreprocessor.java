package ru.enzhine.rnb.texture.preprocessor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.texture.exception.TexturePreprocessorException;

@Builder
@Jacksonized
public class LayeredTexturePreprocessor implements TexturePreprocessor {

    @NonNull
    private final TexturePreprocessor[] layers;

    @Override
    public Texture process() {
        if (layers.length == 0) {
            throw new TexturePreprocessorException("Layers can not be empty");
        }
        Pixmap basePixmap = null;

        for (TexturePreprocessor layer : layers) {
            var texture = layer.process();
            texture.getTextureData().prepare();

            if (basePixmap == null) {
                basePixmap = texture.getTextureData().consumePixmap();
            } else {
                var pixMap = texture.getTextureData().consumePixmap();

                basePixmap.drawPixmap(pixMap, 0, 0);
                pixMap.dispose();
            }
        }

        return new Texture(basePixmap);
    }
}

