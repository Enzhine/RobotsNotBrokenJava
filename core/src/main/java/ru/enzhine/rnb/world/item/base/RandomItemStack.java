package ru.enzhine.rnb.world.item.base;

import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.stateful.StatefulRenderingContext;

import java.util.Random;

public class RandomItemStack extends BasicItemStack {

    public RandomItemStack(TextureRenderer<RenderingContext> textureRenderer, ItemType itemType, int initialCount, int maxCount, Random r, int statesCount) {
        super(textureRenderer, itemType, initialCount, maxCount);
        if (this.itemRendererContext instanceof StatefulRenderingContext src) {
            src.setCurrentState(r.nextInt(statesCount));
        }
    }
}
