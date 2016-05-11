package ohi.andre.keyboardtinter2;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import ohi.andre.keyboardtinter2.app.MainActivity;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.ColorStorage;
import ohi.andre.keyboardtinter2.utils.Keyboards;
import ohi.andre.keyboardtinter2.utils.Utils;

public class KeyboardTinterModule implements IXposedHookLoadPackage {

    private static List<Object> alreadyHooked = new ArrayList<>();

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        Hooker hooker = Keyboards.getHooker(lpparam.packageName);
        if (hooker == null)
            return;

        XposedBridge.log("Hooker: " + hooker.getClass().toString());

        if (Utils.listContainsObject(alreadyHooked, hooker)) {
            return;
        }

        alreadyHooked.add(hooker);

        XSharedPreferences prefs = new XSharedPreferences(getClass().getPackage().getName(), MainActivity.prefs_name);
        List<Integer> cs = ColorStorage.getColors(prefs, MainActivity.color_n_key, MainActivity.color_key);
        Integer[] c = cs.toArray(new Integer[cs.size()]);
        ColorProvider.insertColors(c);

        hooker.hook(lpparam.classLoader);
    }

}