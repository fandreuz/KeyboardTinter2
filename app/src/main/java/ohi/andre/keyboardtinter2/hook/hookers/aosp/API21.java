package ohi.andre.keyboardtinter2.hook.hookers.aosp;

import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

public class API21 implements Hooker {

    private final String PRESSED_FIELDNAME = "mPressed";

    private final String KEY_CLASS = "com.android.inputmethod.keyboard.Key";

    @Override
    public void hook(ClassLoader loader) {

        XposedHelpers.findAndHookMethod(KEY_CLASS, loader,
                "selectBackgroundDrawable", Drawable.class, Drawable.class, Drawable.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        if (!XposedHelpers.getBooleanField(param.thisObject, PRESSED_FIELDNAME))
                            return;

                        param.setResult(ColorProvider.getRandomColorDrawable());
                    }
                });
    }

}


