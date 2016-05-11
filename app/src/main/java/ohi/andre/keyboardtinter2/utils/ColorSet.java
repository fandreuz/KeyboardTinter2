package ohi.andre.keyboardtinter2.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ohi.andre.keyboardtinter2.app.MainActivity;

/**
 * Created by francescoandreuzzi on 09/02/16.
 */
public class ColorSet {

    private static final Integer[] MATERIAL_LIGHT = {
            0xffEF5350,
            0xFFF06292,
            0xFF90CAF9,
            0xFF80CBC4,
            0xFFA5D6A7,
            0xFFFFF176,
            0xFFFFCC80
    };

    private static final Integer[] MATERIAL_DARK = {
            0xFFB71C1C,
            0xFF01579B,
            0xFF00695C,
            0xFF2E7D32,
            0xFF4E342E,
            0xFF455A64
    };

    private static final Integer[] SHINY = {
            0xFF39FF14,
            0xFFFE59C2,
            0xFFF3F315,
            0xFF18FFFF,
            0xFF00E676,
            0xFFFF3D00
    };

    private int mPresetNumber;
    private List<Integer> set;
    private SharedPreferences mPrefs;

    public ColorSet(SharedPreferences p, int set) {
        this.mPrefs = p;
        update(set);
    }

    public void update(int i) {
        this.mPresetNumber = i;
        switch (this.mPresetNumber) {
            case MainActivity.MATERIAL_DARK:
                set = Arrays.asList(MATERIAL_DARK);
                break;
            case MainActivity.MATERIAL_LIGHT:
                set = Arrays.asList(MATERIAL_LIGHT);
                break;
            case MainActivity.SHINY:
                set = Arrays.asList(SHINY);
                break;
            case MainActivity.CUSTOM:
                if (mPrefs.getInt(MainActivity.preset_key, 0) == MainActivity.CUSTOM)
                    set = ColorStorage.getColors(mPrefs, MainActivity.color_n_key, MainActivity.color_key);
                else
                    set = new ArrayList<>();
                break;
        }
    }

    public List<Integer> getColors() {
        return set;
    }

    public int getPresetNumber() {
        return mPresetNumber;
    }

    public void add(int color) {
        set.add(color);
    }

    public void remove(int position) {
        set.remove(position);
    }
}
