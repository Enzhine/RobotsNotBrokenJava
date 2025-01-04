package ru.enzhine.rnb.texture.exception;

public class TextureRendererException extends RuntimeException {
    public TextureRendererException(String message) {
        this(message, null);
    }

    public TextureRendererException(String message, Throwable baseThrowable) {
        super(message, baseThrowable);
    }
}
