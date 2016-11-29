package fr.efaya;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sktifa on 25/11/2016.
 */
public class Constants {
    public static int PAGE = 10;
    public static final String THUMB = "THUMB";
    public static final String PREVIEW = "PREVIEW";

    public static enum ORIGIN {
        ASIA,
        EUROPE,
        NORTH_AMERICA,
        SOUTH_AMERICA,
        AFRICA
    }

    public static final Map<String, Format> formats = new HashMap<String, Format>(){{
        put(THUMB, new Format(50, 50));
        put(PREVIEW, new Format(800, 600));
    }};

    public static class Format {
        int width;
        int height;

        Format(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
