package ru.enzhine.rnb.texture.processor;

import com.badlogic.gdx.graphics.Texture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PathTextureProcessor implements TextureProcessor {

    private String path;

    @Override
    public Texture process() {
        return new Texture(path);
    }
}

