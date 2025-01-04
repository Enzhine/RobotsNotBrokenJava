package ru.enzhine.rnb.texture.outline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.enzhine.rnb.texture.preprocessor.LayeredTexturePreprocessor;
import ru.enzhine.rnb.texture.preprocessor.PathTexturePreprocessor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedOutlineColor.class, name = "fixed"),
        @JsonSubTypes.Type(value = AverageOutlineColor.class, name = "average"),
        @JsonSubTypes.Type(value = NoneOutlineColor.class, name = "none")
})
public interface OutlineColorStrategy {
    Color getColor(Texture texture);
}
