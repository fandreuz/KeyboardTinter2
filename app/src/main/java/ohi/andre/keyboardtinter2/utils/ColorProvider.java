package ohi.andre.keyboardtinter2.utils;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;

import java.util.Random;

import de.robv.android.xposed.XposedBridge;

public class ColorProvider {

    private static Random random = new Random();
    private static ColorDrawable[] drawables;
    private static Integer[] colors = new Integer[]{
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.YELLOW
    };

    public static void insertColors(Integer[] c) {
        if (c != null && c.length > 0)
            colors = c;
        drawables = getColorDrawables(colors);
    }

    private static ColorDrawable[] getColorDrawables(Integer[] colors) {
        ColorDrawable[] ds = new ColorDrawable[colors.length];
        for (int count = 0; count < colors.length; count++)
            ds[count] = new ColorDrawable(colors[count]);
        return ds;
    }

    public static ColorDrawable getRandomColorDrawable() {
        return drawables[random.nextInt(drawables.length)];
    }

    public static int getRandomColor() {
        try {
            return colors[random.nextInt(colors.length)];
        } catch (IllegalArgumentException e) {
            XposedBridge.log("KT2 problem, please try rebooting");
            return 0;
        }
    }

    public static ColorFilter getRandomColorFilter() {
        return new LightingColorFilter(1, getRandomColor());
    }
}


