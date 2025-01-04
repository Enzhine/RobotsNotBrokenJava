package ru.enzhine.rnb.texture;

import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.world.Fluid;
import ru.enzhine.rnb.world.block.base.BiomeType;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Textures {
    private static final HashMap<String, TextureRenderer<RenderingContext>> cache = new LinkedHashMap<>();

    private static TextureRendererFactory textureRendererFactory;

    static {
        textureRendererFactory = new JsonTextureRendererFactory();
    }

    public static TextureRenderer<RenderingContext> getTextureRenderer(String path) {
        if (!cache.containsKey(path)) {
            var textureRenderer = textureRendererFactory.makeRenderer(path);
            textureRenderer.prepare();
            cache.put(path, textureRenderer);
        }
        return cache.get(path);
    }

    public static TextureRenderer<RenderingContext> getBGTextureRenderer(BiomeType biomeType) {
        String path = switch (biomeType) {
            case LIGHT, UNIQUE, HEAVY, DESERT, ORGANIC -> "bg/light_biome.png";
        };
        return getTextureRenderer(path);
    }

    public static TextureRenderer<RenderingContext> getFluidTextureRenderer(Fluid fluid) {
        String path = switch (fluid) {
            case WATER -> "block/fluid/water.png";
            case OIL -> "block/fluid/oil.png";
        };
        return getTextureRenderer(path);
    }
}
