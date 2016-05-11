package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.Canvas;
import android.graphics.Paint;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by francescoandreuzzi on 27/01/16.
 */
public class HTCHooker implements Hooker {

    private final String KVIEW_CLASS = "com.htc.sense.ime.ezsip.KeyboardView";
    private final String KEY_CLASS = "com.htc.sense.ime.ezsip.Keyboard$Key";

    private final String KEYPRESSEDCOLOR_FIELD = "mPressedBgColor";

    private final String DRAWKEYBG_METHOD = "drawKeyBackground";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass(KVIEW_CLASS, loader);
        Class<?> key = XposedHelpers.findClass(KEY_CLASS, loader);

        XposedHelpers.findAndHookMethod(kView, DRAWKEYBG_METHOD, key, Paint.class, Canvas.class, boolean.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                XposedHelpers.setIntField(param.thisObject, KEYPRESSEDCOLOR_FIELD, ColorProvider.getRandomColor());
            }
        });
    }
}
