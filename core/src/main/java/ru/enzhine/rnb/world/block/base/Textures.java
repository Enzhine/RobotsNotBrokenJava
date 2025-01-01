package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Texture;
import ru.enzhine.rnb.utils.TextureUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Textures {
    private static final HashMap<String, TextureCache> cache = new LinkedHashMap<>();

    public static TextureCache getTexture(String path) {
        if (!cache.containsKey(path)) {
            var tex = new Texture(path);
            var textureCache = new TextureCache(
                    tex,
                    TextureUtils.getAverageColor(tex).mul(0.65f)
            );
            cache.put(path, textureCache);
        }
        return cache.get(path);
    }
}
