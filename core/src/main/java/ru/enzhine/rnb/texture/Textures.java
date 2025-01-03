package ru.enzhine.rnb.texture;

import com.badlogic.gdx.graphics.Texture;
import ru.enzhine.rnb.utils.TextureUtils;
import ru.enzhine.rnb.world.Fluid;
import ru.enzhine.rnb.world.Material;
import ru.enzhine.rnb.world.block.base.BiomeType;

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
                originTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
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

    public static TextureCache getBGTexture(BiomeType biomeType) {
        String path = switch (biomeType) {
            case LIGHT, UNIQUE, HEAVY, DESERT, ORGANIC -> "bg/light_biome.png";
        };
        return getTexture(path);
    }

    public static TextureCache getFluidTexture(Fluid fluid) {
        String path = switch (fluid) {
            case WATER -> "block/fluid/water.png";
            case OIL -> "block/fluid/oil.png";
        };
        return getTexture(path);
    }
}
