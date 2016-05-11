package ohi.andre.keyboardtinter2.hook.hookers;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by francescoandreuzzi on 22/01/16.
 */
public class AsusHooker implements Hooker {

    private final String KEY_CLASS = "com.asus.ime.KeyboardEx$Key";

    private final String PRESSED_FIELD = "pressed";

    private final String KEYBG_METHOD = "getKeyBackground";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> key = XposedHelpers.findClass(KEY_CLASS, loader);

        XposedHelpers.findAndHookMethod(key, KEYBG_METHOD, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                if (XposedHelpers.getBooleanField(param.thisObject, PRESSED_FIELD))
                    param.setResult(ColorProvider.getRandomColorDrawable());
            }
        });
    }
}
