package ru.enzhine.rnb.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class TextureUtils {

    public static Color getAverageColor(Texture texture) {
        var td = texture.getTextureData();
        if (!td.isPrepared()) {
            td.prepare();
        }
        var pixmap = td.consumePixmap();
        var tempColor = new Color();
        float r = 0;
        float g = 0;
        float b = 0;
        float a = 0;
        for (int x = 0; x < texture.getWidth(); ++x) {
            for (int y = 0; y < texture.getHeight(); ++y) {
                Color.rgba8888ToColor(tempColor, pixmap.getPixel(x, y));
                r += tempColor.r;
                g += tempColor.g;
                b += tempColor.b;
                a += tempColor.a;
            }
        }
        float n = texture.getWidth() * texture.getHeight();
        tempColor.set(r / n, g / n, b / n, a / n);
        return tempColor;
    }
}
