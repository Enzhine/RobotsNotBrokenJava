package ru.enzhine.rnb.editor;

import com.badlogic.gdx.Input;
import lombok.AllArgsConstructor;
import ru.enzhine.rnb.editor.input.ShortcutInputAdapter;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

@AllArgsConstructor
public class CodeEditorInputProcessor extends ShortcutInputAdapter {

    private final CodeEditor codeEditor;
//    private final NashornScriptEngine engine;

    @Override
    public boolean keyDown(int keycode) {
        System.out.println(keycode);
        switch (keycode) {
            case Input.Keys.ENTER -> codeEditor.onNewline();
            case Input.Keys.UP -> codeEditor.up();
            case Input.Keys.DOWN -> codeEditor.down();
            case Input.Keys.LEFT -> codeEditor.left();
            case Input.Keys.RIGHT -> codeEditor.right();
//            case Input.Keys.ESCAPE -> processScript(); // TODO: TEMPORARY
            default -> super.keyDown(keycode);
        }
        return true;
    }

    @Override
    public boolean shortcutExecuted(int keycode) {
        switch (keycode) {
            case Input.Keys.C -> copyToClipboard();
            case Input.Keys.V -> pasteFromClipboard();
            default -> super.shortcutExecuted(keycode);
        }
        return true;
    }

    @Override
    public boolean regularKeyTyped(char character) {
        switch (character) {
            case '\b' -> codeEditor.onBackspace();
            case '\t' -> codeEditor.onPaste("   ");
            default -> codeEditor.onPaste(String.valueOf(character));
        }
        return true;
    }

    private void copyToClipboard() {
        try {
            StringSelection copy = new StringSelection(codeEditor.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(copy, copy);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void pasteFromClipboard() {
        try {
            String paste = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            codeEditor.onPaste(paste);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

//    private void processScript() {
//        String text = codeEditor.getText();
//        System.out.println(text);
//        try {
//            final var cs = engine.compile(text);
//
//            var thread = new Thread(() -> {
//                try {
//                    cs.eval();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            thread.start();
//        } catch (ScriptException e) {
//            throw new RuntimeException(e);
//        }
//        codeEditor.setText("");
//    }
}
