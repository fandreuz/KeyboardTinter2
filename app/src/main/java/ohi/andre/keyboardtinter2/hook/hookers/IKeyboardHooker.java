package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;
import ohi.andre.reflectionutils.ReflectionUtils;

/**
 * Created by andre on 15/11/15.
 */
public class IKeyboardHooker implements Hooker {

    private final String KEYBOARDVIEW_CLASS = "com.qisi.inputmethod.keyboard.KeyboardView";
    private final String KEY_CLASS = "com.qisi.inputmethod.keyboard.f";

    private final String BG_FIELD = "a_";
    private final String NATIVEBG_FIELD = "nativeBg";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kview = XposedHelpers.findClass(KEYBOARDVIEW_CLASS, loader);
        Class<?> key = XposedHelpers.findClass(KEY_CLASS, loader);

        Method m = XposedHelpers.findMethodsByExactParameters(kview, null, key, Canvas.class, Paint.class)[0];
        Method[] ms = XposedHelpers.findMethodsByExactParameters(key, int[].class);

        Method getState;
//        if(ms != null && ms.length > 0)
//            getState = ms[0];
//        else
        getState = XposedHelpers.findMethodBestMatch(key, "N");

        final Method gs = getState;

        XposedBridge.hookMethod(m, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                if (XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD) == null)
                    XposedHelpers.setAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD,
                            XposedHelpers.getObjectField(param.thisObject, BG_FIELD));

                Integer[] state = Utils.toIntegerArray((int[]) XposedHelpers.callMethod(param.args[0], gs.getName()));
                if (ReflectionUtils.arrayContains(state, android.R.attr.state_pressed))
                    XposedHelpers.setObjectField(param.thisObject, BG_FIELD, ColorProvider.getRandomColorDrawable());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                XposedHelpers.setObjectField(param.thisObject, BG_FIELD,
                        XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD));
            }
        });
    }
}
