package ru.enzhine.rnb.texture;

import ru.enzhine.rnb.texture.processor.TextureProcessor;

import java.io.IOException;

public interface TextureProcessorFactory {
    TextureProcessor getProcessor(String configurationPath) throws IOException;
}
