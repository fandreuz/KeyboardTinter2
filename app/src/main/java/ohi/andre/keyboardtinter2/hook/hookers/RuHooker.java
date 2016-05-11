package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;

/**
 * Created by francescoandreuzzi on 22/01/16.
 */
public class RuHooker implements Hooker {

    private final String KVIEW_CLASS = "ru.androidteam.rukeyboard.view.KeyboardView";

    private final String KEYBG_FIELD = "mKeyBackground";
    private final String INVALIDATEDKEY_FIELD = "mInvalidatedKey";

    private final String DRAW_METHOD = "onBufferDraw";
    private final String STATE_METHOD = "getCurrentDrawableState";

    private Drawable nativeBackground = null;

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass(KVIEW_CLASS, loader);

        XposedHelpers.findAndHookMethod(kView, DRAW_METHOD, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                if (nativeBackground == null)
                    nativeBackground = (Drawable) XposedHelpers.getObjectField(param.thisObject, KEYBG_FIELD);

                Object key = XposedHelpers.getObjectField(param.thisObject, INVALIDATEDKEY_FIELD);
                if (key == null)
                    return;

                int[] state = (int[]) XposedHelpers.callMethod(key, STATE_METHOD);
                if (!Utils.containsInt(state, android.R.attr.state_pressed))
                    return;

                XposedHelpers.setObjectField(param.thisObject, KEYBG_FIELD, ColorProvider.getRandomColorDrawable());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                XposedHelpers.setObjectField(param.thisObject, KEYBG_FIELD, nativeBackground);
            }
        });
    }
}
