package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Textures {
    private static final HashMap<String, Texture> cache = new LinkedHashMap<>();

    public static Texture getTexture(String path) {
        if (!cache.containsKey(path)) {
            cache.put(path, new Texture(path));
        }
        return cache.get(path);
    }


}
