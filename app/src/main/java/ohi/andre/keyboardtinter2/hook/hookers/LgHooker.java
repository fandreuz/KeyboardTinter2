package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;

/**
 * Created by francescoandreuzzi on 04/02/16.
 */
public class LgHooker implements Hooker {

    private final String SPECIALKEY_FIELD = "K";

    private final String DRAWABLESTATE_METHODNAME = "getCurrentDrawableState";

    private final String KEY_CLASS = "com.lge.ime.keyboard.LgeKeyboard$Key";
    private final String KEYBOARDVIEW_CLASS = "com.lge.ime.inputview.LgeKeyboardViewBase";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass(KEYBOARDVIEW_CLASS, loader);
        final Class<?> key = XposedHelpers.findClass(KEY_CLASS, loader);

        Method method = XposedHelpers.findMethodsByExactParameters(kView, Drawable.class, key)[0];

        XposedBridge.hookMethod(method, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Object k = param.args[0];
                if (k == null)
                    return;

                boolean isSpecialKey = XposedHelpers.getBooleanField(k, SPECIALKEY_FIELD);

                int[] drawableState;
                try {
                    drawableState = (int[]) XposedHelpers.callMethod(k, DRAWABLESTATE_METHODNAME);
                } catch (Exception | Error e) {
                    return;
                }
                boolean pressed = Utils.containsInt(drawableState, android.R.attr.state_pressed);

                if (isSpecialKey || pressed)
                    param.setResult(ColorProvider.getRandomColorDrawable());
            }
        });

    }

}
