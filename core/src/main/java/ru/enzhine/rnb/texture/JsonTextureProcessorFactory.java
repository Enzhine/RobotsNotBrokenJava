package ru.enzhine.rnb.texture;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.enzhine.rnb.texture.processor.PathTextureProcessor;
import ru.enzhine.rnb.texture.processor.TextureProcessor;

import java.io.IOException;
import java.util.Arrays;

public class JsonTextureProcessorFactory implements TextureProcessorFactory {

    private static final String JSON_EXTENSION = ".json";
    private static final String[] RAW_TEXTURE_EXTENSION = {".png"};

    private final ClassLoader classLoader;
    private final ObjectMapper objectMapper;

    public JsonTextureProcessorFactory() {
        this.classLoader = this.getClass().getClassLoader();
        this.objectMapper = new ObjectMapper();
    }

    private boolean isJsonConfiguration(String configurationPath) {
        return configurationPath.endsWith(JSON_EXTENSION);
    }

    private boolean isRawTextureFile(String texturePath) {
        return Arrays.stream(RAW_TEXTURE_EXTENSION).anyMatch(texturePath::endsWith);
    }

    @Override
    public TextureProcessor getProcessor(String configurationPath) throws IOException {
        if (isJsonConfiguration(configurationPath)) {
            var resource = this.classLoader.getResource(configurationPath);

            return objectMapper.readValue(resource, TextureProcessor.class);
        }else if (isRawTextureFile(configurationPath)) {
            return new PathTextureProcessor(configurationPath);
        }else {
            throw new RuntimeException("Not a JSON configuration nor RAW TEXTURE extension.");
        }
    }
}
