package ru.enzhine.rnb.editor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class ShortcutInputAdapter extends InputAdapter {

    public boolean shortcutExecuted(int keycode) {
        return false;
    }

    public boolean regularKeyTyped(char character) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            return false;
        }
        if (keycode == Input.Keys.ENTER) {
            return false;
        }
        if (isShortcutEnabled()) {
            return shortcutExecuted(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == ' ' && isShortcutEnabled()) {
            return shortcutExecuted(Input.Keys.SPACE);
        }
        if (character == '\n' && isShortcutEnabled()) {
            return shortcutExecuted(Input.Keys.ENTER);
        }
        return regularKeyTyped(character);
    }

    private boolean isShortcutEnabled() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
    }
}
