package ru.enzhine.rnb.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CodeEditor extends Widget implements TextEditor, KeyboardListener {

    private final BitmapFont bitmapFont;
    private final LinkedList<StringBuilder> lines;
    private CurrentListIterator<StringBuilder> currentLineIterator;
    private StringBuilder currentLine;
    private int position;

    public CodeEditor() {
        super();

        this.bitmapFont = new BitmapFont();
        this.bitmapFont.getData().markupEnabled = true;

        this.lines = new LinkedList<>();
        reset();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int lineId = 0;
        float lineH = bitmapFont.getLineHeight();
        float x = getX();
        float y = getY();

        for (StringBuilder line : lines) {
            this.bitmapFont.draw(batch, String.valueOf(lineId + 1), x, y - lineId * lineH, 0, 0, false);
            if (lineId != currentLineIterator.index()) {
                this.bitmapFont.draw(batch, line, x + 10, y - lineId * lineH);
            } else {
                var out = new StringBuilder(line);
                out.insert(position, "|");
                this.bitmapFont.draw(batch, out, x + 10, y - lineId * lineH);
            }
            lineId += 1;
        }
    }

    private void reset() {
        this.lines.add(new StringBuilder());
        this.position = 0;
        this.currentLineIterator = new CurrentListIterator<>(this.lines.listIterator());
        this.currentLine = this.currentLineIterator.get();
    }

    public void dispose() {
        this.bitmapFont.dispose();
    }

    @Override
    public void setText(String text) {
        lines.clear();
        position = 0;
        text.lines().forEach(line -> {
            lines.add(new StringBuilder(line));
        });
        if (lines.isEmpty()) {
            reset();
        }
    }

    @Override
    public String getText() {
        return String.join(System.lineSeparator(), lines);
    }

    @Override
    public void onBackspace() {
        if (position != 0) {
            currentLine.delete(position - 1, position);
            position -= 1;
        } else if (currentLineIterator.index() != 0) {
            String other = currentLine.substring(position);
            currentLineIterator.remove();
            currentLine = currentLineIterator.get();
            position = currentLine.length();
            pasteString(other);
            position -= other.length();
        }
    }

    @Override
    public void onNewline() {
        String other = currentLine.substring(position);
        currentLine.delete(position, currentLine.length());
        currentLineIterator.add(new StringBuilder(other));
        currentLine = currentLineIterator.get();
        position = 0;
    }

    private void pasteString(String paste) {
        currentLine.insert(position, paste);
        position += paste.length();
    }

    @Override
    public void onPaste(String paste) {
        AtomicBoolean first = new AtomicBoolean(true);
        paste.lines().forEach(line -> {
            if (first.get()) {
                first.set(false);
            } else {
                onNewline();
            }
            pasteString(line);
        });
    }

    @Override
    public void up() {
        if (currentLineIterator.hasPrevious()) {
            currentLineIterator.previous();
            var prevLine = currentLineIterator.get();
            if (position > prevLine.length()) {
                position = prevLine.length();
            }
            currentLine = prevLine;
        } else {
            position = 0;
        }
    }

    @Override
    public void down() {
        if (currentLineIterator.hasNext()) {
            currentLineIterator.next();
            var nextLine = currentLineIterator.get();
            if (position > nextLine.length()) {
                position = nextLine.length();
            }
            currentLine = nextLine;
        } else {
            position = currentLine.length();
        }
    }

    @Override
    public void left() {
        if (position > 0) {
            position -= 1;
        } else {
            var skip = currentLineIterator.hasPrevious();
            up();
            if (skip) {
                position = currentLine.length();
            }
        }
    }

    @Override
    public void right() {
        if (position < currentLine.length()) {
            position += 1;
        } else {
            var skip = currentLineIterator.hasNext();
            down();
            if (skip) {
                position = 0;
            }
        }
    }

    @Override
    public void undo() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void redo() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setCursor(int line, int pos) {
        throw new RuntimeException("Not implemented yet");
    }
}
