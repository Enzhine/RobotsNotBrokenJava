package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

public interface Rendering {
    void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport);
}
