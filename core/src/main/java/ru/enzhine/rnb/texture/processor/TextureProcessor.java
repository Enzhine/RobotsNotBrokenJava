package ru.enzhine.rnb.texture.processor;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PathTextureProcessor.class, name = "path"),
        @JsonSubTypes.Type(value = LayeredTextureProcessor.class, name = "layered")
})
public interface TextureProcessor {
    Texture process();
}
