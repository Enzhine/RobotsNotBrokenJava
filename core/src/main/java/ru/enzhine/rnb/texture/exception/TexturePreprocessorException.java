package ru.enzhine.rnb.texture.exception;

public class TexturePreprocessorException extends RuntimeException {
    public TexturePreprocessorException(String message) {
        this(message, null);
    }

    public TexturePreprocessorException(String message, Throwable baseThrowable) {
        super(message, baseThrowable);
    }
}
