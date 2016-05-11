package ohi.andre.keyboardtinter2.hook.hookers.aosp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

public class API14 implements Hooker {

    private final String BGTYPE_FIELDNAME = "mBackgroundType";
    private final String BG_FIELDNAME = "mKeyBackground";
    private final String PRESSED_FIELDNAME = "mPressed";

    private final String KEYBOARDVIEW_CLASS = "com.android.inputmethod.keyboard.KeyboardView";
    private final String KEY_CLASS = "com.android.inputmethod.keyboard.Key";
    private final String KEYBOARDID_CLASS = "com.android.inputmethod.keyboard.Keyboard";
    private final String KDP_CLASS = "com.android.inputmethod.keyboard.KeyboardView$KeyDrawParams";

    private final int NORMAL_KEY = 0;

    private Drawable nativeBackground;

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kViewClass;
        Class<?> keyClass;
        Class<?> keyboardIdClass;
        Class<?> kdpClass;
        try {
            kViewClass = loader.loadClass(KEYBOARDVIEW_CLASS);
            keyClass = loader.loadClass(KEY_CLASS);
            keyboardIdClass = loader.loadClass(KEYBOARDID_CLASS);
            kdpClass = loader.loadClass(KDP_CLASS);
        } catch (ClassNotFoundException e) {
            return;
        }

        XposedHelpers.findAndHookMethod(
                kViewClass,
                "onBufferDrawKey",
                keyClass,
                keyboardIdClass,
                Canvas.class,
                Paint.class,
                kdpClass,
                boolean.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        if (nativeBackground == null)
                            nativeBackground = (Drawable) XposedHelpers.getObjectField(param.thisObject, BG_FIELDNAME);

                        if (XposedHelpers.getIntField(param.args[0], BGTYPE_FIELDNAME) != NORMAL_KEY)
                            return;

                        if (!XposedHelpers.getBooleanField(param.args[0], PRESSED_FIELDNAME))
                            return;

                        XposedHelpers.setObjectField(param.args[4], BG_FIELDNAME, ColorProvider.getRandomColorDrawable());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        if (nativeBackground != null)
                            XposedHelpers.setObjectField(param.args[4], BG_FIELDNAME, nativeBackground);
                    }
                });
    }

}


