package ru.enzhine.rnb.texture.preprocessor;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PathTexturePreprocessor.class, name = "path"),
        @JsonSubTypes.Type(value = LayeredTexturePreprocessor.class, name = "layered")
})
public interface TexturePreprocessor {
    Texture process();
}
