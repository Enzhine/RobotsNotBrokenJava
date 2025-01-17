package ru.enzhine.rnb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.enzhine.rnb.stages.WorldStage;

public class GameHolder extends ApplicationAdapter {

    private SpriteBatch batch;
    private WorldStage worldStage;
//    private CodeEditorStage codeEditorStage;

    @Override
    public void resize(int width, int height) {
        worldStage.resize(width, height);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        worldStage = new WorldStage(batch);
        Gdx.input.setInputProcessor(worldStage);
//        codeEditorStage = new CodeEditorStage();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        worldStage.update();
        worldStage.draw();
//        codeEditorStage.act(Gdx.graphics.getDeltaTime());
//        codeEditorStage.draw();
    }

    @Override
    public void dispose() {
        worldStage.dispose();
    }
}