package ohi.andre.keyboardtinter2.hook.hookers.aosp;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

public class API17 implements Hooker {

    private final String BGTYPE_FIELDNAME = "mBackgroundType";
    private final String BG_FIELDNAME = "mKeyBackground";
    private final String PRESSED_FIELDNAME = "mPressed";

    private final String KEYBOARDVIEW_CLASS = "com.android.inputmethod.keyboard.KeyboardView";
    private final String KEY_CLASS = "com.android.inputmethod.keyboard.Key";

    private final int NORMAL_KEY = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? 1 : 0;

    private Drawable nativeDrawable;

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kViewClass;
        Class<?> keyClass;
        try {
            kViewClass = loader.loadClass(KEYBOARDVIEW_CLASS);
            keyClass = loader.loadClass(KEY_CLASS);
        } catch (ClassNotFoundException e) {
            return;
        }

        XposedHelpers.findAndHookMethod(
                kViewClass,
                "onDrawKeyBackground",
                keyClass,
                Canvas.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (nativeDrawable == null)
                            nativeDrawable = (Drawable) XposedHelpers.getObjectField(param.thisObject, BG_FIELDNAME);

                        if (XposedHelpers.getIntField(param.args[0], BGTYPE_FIELDNAME) != NORMAL_KEY)
                            return;

                        if (!XposedHelpers.getBooleanField(param.args[0], PRESSED_FIELDNAME))
                            return;

                        XposedHelpers.setObjectField(param.thisObject, BG_FIELDNAME, ColorProvider.getRandomColorDrawable());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        if (nativeDrawable != null)
                            XposedHelpers.setObjectField(param.thisObject, BG_FIELDNAME, nativeDrawable);
                    }
                });
    }
}


