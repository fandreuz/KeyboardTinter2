package ohi.andre.keyboardtinter2.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class ColorStorage {

    public static List<Integer> getColors(SharedPreferences prefs, String keySize, String key) {
        int size = prefs.getInt(keySize, 0);
        List<Integer> colors = new ArrayList<>(size);
        for (int count = 0; count < size; count++) {
            colors.add(prefs.getInt(key + count, 0));
        }
        return colors;
    }

    public static void storeColors(SharedPreferences.Editor edit, List<Integer> colors, String nKey,
                                   String key) {

        for (int count = 0; count < colors.size(); count++)
            edit.putInt(key + count, colors.get(count));
        edit.putInt(nKey, colors.size());
    }

}
