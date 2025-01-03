package ru.enzhine.rnb.texture.processor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LayeredTextureProcessor implements TextureProcessor {

    private TextureProcessor[] layers;

    @Override
    public Texture process() {
        if (layers.length == 0) {
            throw new RuntimeException("Layers can not be empty");
        }
        Pixmap basePixmap = null;

        for (TextureProcessor layer : layers) {
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

