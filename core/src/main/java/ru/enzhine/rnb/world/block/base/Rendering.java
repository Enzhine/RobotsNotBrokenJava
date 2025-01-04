package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Rendering {
    void batchRender(SpriteBatch batch, Viewport viewport);
    void shapeRender(ShapeRenderer renderer, Viewport viewport);
}
