package ru.enzhine.rnb.world.block;

import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.texture.render.stateful.StatefulRenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;
import ru.enzhine.rnb.world.block.base.*;

public class AirBlock extends FloodableBlock {
    public AirBlock(Location loc, BiomeType biomeType) {
        super(Textures.getTextureRenderer("block/air.png"), Textures.getBGTextureRenderer(biomeType), loc, BlockType.AIR, Material.DYNAMIC, biomeType);
    }

    @Override
    public boolean shouldRenderBlockTexture() {
        return false;
    }

    @Override
    public boolean shouldRenderOutline() {
        return false;
    }
}
