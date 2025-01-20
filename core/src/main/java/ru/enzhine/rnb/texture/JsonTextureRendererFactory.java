package ru.enzhine.rnb.texture;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.enzhine.rnb.texture.exception.TextureRendererException;
import ru.enzhine.rnb.texture.preprocessor.PathTexturePreprocessor;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.statical.StaticTextureRenderer;
import ru.enzhine.rnb.texture.render.TextureRenderer;

import java.io.IOException;
import java.util.Arrays;

public class JsonTextureRendererFactory implements TextureRendererFactory {

    private static final String JSON_EXTENSION = ".json";
    private static final String[] RAW_TEXTURE_EXTENSION = {".png"};

    private final ClassLoader classLoader;
    private final ObjectMapper objectMapper;

    public JsonTextureRendererFactory() {
        this.classLoader = this.getClass().getClassLoader();
        this.objectMapper = new ObjectMapper();
    }

    private boolean isJsonConfiguration(String configurationPath) {
        return configurationPath.endsWith(JSON_EXTENSION);
    }

    private boolean isRawTextureFile(String texturePath) {
        return Arrays.stream(RAW_TEXTURE_EXTENSION).anyMatch(texturePath::endsWith);
    }

    private TextureRenderer<RenderingContext> rawTextureFileRenderer(String texturePath) {
        return StaticTextureRenderer.builder()
                .src(PathTexturePreprocessor.builder().path(texturePath).build())
                .build();
    }

    @Override
    public TextureRenderer<RenderingContext> makeRenderer(String rendererConfigPath) {
        if (isJsonConfiguration(rendererConfigPath)) {
            var resource = this.classLoader.getResource(rendererConfigPath);

            try {
                return objectMapper.readValue(resource, TextureRenderer.class);
            } catch (IOException ex) {
                throw new TextureRendererException(String.format("Unable to parse configuration %s", rendererConfigPath), ex);
            }
        } else if (isRawTextureFile(rendererConfigPath)) {
            return rawTextureFileRenderer(rendererConfigPath);
        } else {
            throw new TextureRendererException("Not a JSON configuration nor RAW TEXTURE extension.");
        }
    }
}
