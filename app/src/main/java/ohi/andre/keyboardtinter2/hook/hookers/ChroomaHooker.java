package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by francescoandreuzzi on 30/01/16.
 */
public class ChroomaHooker implements Hooker {

    private final String KEY_CLASS = "com.android.inputmethod.keyboard.Key";

    private final String BGDRAWABLE_METHOD = "selectBackgroundDrawable";

    private final String PRESSED_FIELDNAME = "mPressed";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> keyClass = XposedHelpers.findClass(KEY_CLASS, loader);

        XposedHelpers.findAndHookMethod(keyClass, BGDRAWABLE_METHOD, Drawable.class, Drawable.class, Drawable.class, int.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        if (!XposedHelpers.getBooleanField(param.thisObject, PRESSED_FIELDNAME))
                            return;

                        param.setResult(ColorProvider.getRandomColorDrawable());
                    }
                });
    }
}
