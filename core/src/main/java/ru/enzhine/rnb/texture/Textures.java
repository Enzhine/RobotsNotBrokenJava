package ru.enzhine.rnb.texture;

import ru.enzhine.rnb.utils.TextureUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Textures {
    private static final HashMap<String, TextureCache> cache = new LinkedHashMap<>();

    private static TextureProcessorFactory textureProcessorFactory;

    static {
        textureProcessorFactory = new JsonTextureProcessorFactory();
    }

    public static TextureCache getTexture(String path) {
        if (!cache.containsKey(path)) {
            try {
                var textureProcessor = textureProcessorFactory.getProcessor(path);
                var originTexture = textureProcessor.process();
                var outlineColor = TextureUtils.getAverageColor(originTexture).mul(0.65f);

                cache.put(path, new TextureCache(
                        textureProcessor,
                        originTexture,
                        outlineColor
                ));
            } catch (IOException ex) {
                throw new RuntimeException(String.format("Unable to handle texture %s", path), ex);
            }
        }
        return cache.get(path);
    }
}
