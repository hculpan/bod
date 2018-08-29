package org.culpan.bod;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
    public static final String IMMORTAL_32 = "Immortal-32";

    public static final String IMMORTAL_20 = "Immortal-20";

    public static final String IMMORTAL_14 = "Immortal-14";

    static Map<String, BitmapFont> fonts = new HashMap<String, BitmapFont>();

    private static String buildKey(String name, int size) {
        return name + "-" + Integer.toString(size);
    }

    public static void addFont(String keyName, BitmapFont font) {
        fonts.put(keyName, font);
    }

    public static void addFont(String name, int size, BitmapFont font) {
        addFont(buildKey(name, size), font);
    }

    public static BitmapFont getFont(String name, int size) {
        return fonts.get(buildKey(name, size));
    }

    public static BitmapFont getFont(String keyName) {
        return fonts.get(keyName);
    }

    public static BitmapFont addFontFromFile(String filename, String name, int size) {
        return addFontFromFile(filename, size, buildKey(name, size));
    }

    public static BitmapFont addFontFromFile(String filename, int size, String keyName) {
        BitmapFont bitmapFont;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose();
        addFont(keyName, bitmapFont);
        return bitmapFont;
    }

    public static void dispose() {
        fonts.forEach( (s, f) -> f.dispose() );
    }
}
