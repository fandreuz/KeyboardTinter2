package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.drawable.Drawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;
import ohi.andre.reflectionutils.ReflectionUtils;

/**
 * Created by andre on 11/11/15.
 */
public class BlackberryHooker implements Hooker {

    private final String CLASS = "com.blackberry.inputmethod.keyboard.a";
    private final String B_CLASS = "com.blackberry.inputmethod.keyboard.b";

    private final String B_FIELD = "b";
    private final String Q_FIELD = "q";

    private final String A_STATIC_FIELD = "a";

    private final String A_METHOD = "a";

    @Override
    public void hook(final ClassLoader loader) {
        Class<?> c = XposedHelpers.findClass(CLASS, loader);

        Class<?> b = XposedHelpers.findClass(B_CLASS, loader);
        final Object[] o = (Object[]) XposedHelpers.getStaticObjectField(b, A_STATIC_FIELD);

        XposedHelpers.findAndHookMethod(c, "a", Drawable.class, Drawable.class, Drawable.class, Drawable.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Object obj = o[XposedHelpers.getIntField(param.thisObject, B_FIELD)];
                Integer[] state = Utils.toIntegerArray((int[]) XposedHelpers.callMethod(obj, A_METHOD,
                        XposedHelpers.getBooleanField(param.thisObject, Q_FIELD)));

                if (!ReflectionUtils.arrayContains(state, android.R.attr.state_pressed))
                    return;

                param.args[0] = ColorProvider.getRandomColorDrawable();
                param.args[1] = ColorProvider.getRandomColorDrawable();
                param.args[2] = ColorProvider.getRandomColorDrawable();
                param.args[3] = ColorProvider.getRandomColorDrawable();
            }
        });
    }
}
