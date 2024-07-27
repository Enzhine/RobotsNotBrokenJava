package ru.enzhine.rnb.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Rendering {
    void render(SpriteBatch batch, Viewport viewport);
}
