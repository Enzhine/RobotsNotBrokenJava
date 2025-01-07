package ru.enzhine.rnb.editor;

public interface KeyboardListener {
    void onBackspace();
    void onNewline();
    void onPaste(String paste);
    void up();
    void down();
    void left();
    void right();
    void undo();
    void redo();
    void setCursor(int line, int pos);
}
