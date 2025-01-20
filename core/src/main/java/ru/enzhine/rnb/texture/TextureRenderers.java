package ru.enzhine.rnb.texture;

import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.world.Fluid;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.item.base.ItemType;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TextureRenderers {
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

    public static TextureRenderer<RenderingContext> getItemTextureRenderer(ItemType itemType) {
        String path = switch (itemType) {
            case SOIL_PIECE -> "item/soil_piece.json";
            case RICH_SOIL_PIECE -> "item/rich_soil_piece.json";
            case SAND_PIECE -> "item/sand_piece.json";
            case SOFT_STONE_SHARD -> "item/soft_stone_shard.json";
            case HARD_STONE_SHARD -> "item/hard_stone_shard.json";
            case STONE_SHARD -> "item/stone_shard.json";
            case COPPER_ORE_INGOT -> "item/copper_shard.json";
            case COAL_ORE_SHARD -> "item/coal_shard.json";
            case SEAWEED_PIECE -> "item/seaweed_piece.json";
            case DRY_SEAWEED_PIECE -> "item/dry_seaweed_piece.json";
            case OXYLITTE_SHARD -> "item/oxylitte_shard.json";
            case DRILL -> "TODO";
        };
        return getTextureRenderer(path);
    }
}
