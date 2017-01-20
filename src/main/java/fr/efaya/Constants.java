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
        put(THUMB, new Format(300, 225));
        put(PREVIEW, new Format(800, 600));
    }};

    public static class Format {
        int width;
        int height;

        public Format(int width, int height) {
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

    public static Format getScaledDimension(Format imgSize, Format boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Format(new_width, new_height);
    }
}
