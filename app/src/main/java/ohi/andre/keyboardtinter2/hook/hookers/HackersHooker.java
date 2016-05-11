package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by andre on 21/10/15.
 */
public class HackersHooker implements Hooker {

    private final String KEYBOARDVIEW_CLASS = "org.pocketworkstation.pckeyboard.LatinKeyboardBaseView";

    private final String KEYBOARDDRAW_METHOD = "onBufferDraw";

    private final String KEYBG_FIELD = "mKeyBackground";
    private final String INVALIDATEDKEY_FIELD = "mInvalidatedKey";
    private final String NATIVEBG_FIELD = "nativeBg";
    private final String PRESSED_FIELD = "pressed";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> keyboardView = XposedHelpers.findClass(KEYBOARDVIEW_CLASS, loader);

        XposedHelpers.findAndHookMethod(keyboardView, KEYBOARDDRAW_METHOD, Canvas.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                if (param.thisObject == null)
                    return;
                Object o = XposedHelpers.getObjectField(param.thisObject, INVALIDATEDKEY_FIELD);

                Drawable nativeDrawable = (Drawable) XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD);
                if (nativeDrawable == null) {
                    nativeDrawable = (Drawable) XposedHelpers.getObjectField(param.thisObject, KEYBG_FIELD);
                    XposedHelpers.setAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD, nativeDrawable);
                }

                if (o == null || !(Boolean) XposedHelpers.getObjectField(o, PRESSED_FIELD))
                    return;
                XposedHelpers.setObjectField(param.thisObject, KEYBG_FIELD, ColorProvider.getRandomColorDrawable());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                XposedHelpers.setObjectField(param.thisObject, KEYBG_FIELD,
                        XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD));
            }
        });
    }
}
