package ru.enzhine.rnb.texture.preprocessor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class PartTexturePreprocessor implements TexturePreprocessor {

    @NonNull
    private final TexturePreprocessor from;

    @NonNull
    private final Integer x;
    @NonNull
    private final Integer y;
    @NonNull
    private final Integer width;
    @NonNull
    private final Integer height;

    @Override
    public Texture process() {
        var tex = from.process();
        tex.getTextureData().prepare();
        var pixmap = tex.getTextureData().consumePixmap();
        var newPixmap = new Pixmap(width, height, pixmap.getFormat());
        newPixmap.drawPixmap(pixmap, 0, 0, x, y, width, height);
        pixmap.dispose();
        return new Texture(newPixmap);
    }
}

