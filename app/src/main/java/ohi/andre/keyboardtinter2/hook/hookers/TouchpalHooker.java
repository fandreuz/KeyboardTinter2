package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by andre on 30/10/15.
 */
public class TouchpalHooker implements Hooker {

    private final String KEY_CLASS = "com.cootek.smartinput5.ui.cm";
    private final String KEY_CLASS2 = "com.cootek.smartinput5.ui.cl";

    private final String BG_FIELD = "background";

    private final String ONPRESSED_METHOD = "onPress";
    private final String ONRELEASE_METHOD = "onRelease";

    private Drawable nativeBackground;

    @Override
    public void hook(ClassLoader loader) {
        try {
            hook(XposedHelpers.findClass(KEY_CLASS, loader));
        } catch (NoSuchMethodError error) {
            hook(XposedHelpers.findClass(KEY_CLASS2, loader));
        }
    }

    private void hook(Class<?> key) {
        XposedHelpers.findAndHookMethod(key, ONPRESSED_METHOD, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (nativeBackground == null)
                    nativeBackground = (Drawable) XposedHelpers.getObjectField(param.thisObject, BG_FIELD);

                XposedHelpers.setObjectField(param.thisObject, BG_FIELD, ColorProvider.getRandomColorDrawable());
            }
        });

        XposedHelpers.findAndHookMethod(key, ONRELEASE_METHOD, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                XposedHelpers.setObjectField(param.thisObject, BG_FIELD, nativeBackground);
            }
        });
    }
}
