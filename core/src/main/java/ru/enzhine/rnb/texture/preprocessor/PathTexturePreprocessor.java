package ru.enzhine.rnb.texture.preprocessor;

import com.badlogic.gdx.graphics.Texture;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class PathTexturePreprocessor implements TexturePreprocessor {

    @NonNull
    private final String path;

    @Override
    public Texture process() {
        return new Texture(path);
    }
}

