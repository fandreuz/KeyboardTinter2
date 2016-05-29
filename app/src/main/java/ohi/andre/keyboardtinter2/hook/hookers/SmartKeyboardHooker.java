package ohi.andre.keyboardtinter2.hook.hookers;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;
import ohi.andre.reflectionutils.ReflectionUtils;

/**
 * Created by andre on 25/10/15.
 */
public class SmartKeyboardHooker implements Hooker {

    private final String KVIEW_PRO_CLASS = "net.cdeguet.smartkeyboardpro.KeyboardView";

    private final String DRAWKEYS_METHOD = "n";
    private final String KEYSTATE_METHOD = "b";

    private final String INVALIDATEDKEY_FIELD = "aH";
    private final String NATIVEBG_FIELD = "nativeBg";
    private final String NATIVEFCTBG_FIELD = "nativeFctBg";
    private final String NORMALKEYBG_FIELD = "aR";
    private final String FCTKEYBG_FIELD = "aS";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass(KVIEW_PRO_CLASS, loader);

        XposedHelpers.findAndHookMethod(kView, DRAWKEYS_METHOD, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Object key = XposedHelpers.getObjectField(param.thisObject, INVALIDATEDKEY_FIELD);

                if (XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD) == null)
                    XposedHelpers.setAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD,
                            XposedHelpers.getObjectField(param.thisObject, NORMALKEYBG_FIELD));

                if (XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEFCTBG_FIELD) == null)
                    XposedHelpers.setAdditionalInstanceField(param.thisObject, NATIVEFCTBG_FIELD,
                            XposedHelpers.getObjectField(param.thisObject, FCTKEYBG_FIELD));

                Integer[] state = key == null ? null : Utils.toIntegerArray((int[]) XposedHelpers.callMethod(key, KEYSTATE_METHOD));
                if (key == null || !ReflectionUtils.arrayContains(state, android.R.attr.state_pressed))
                    return;
                XposedHelpers.setObjectField(param.thisObject, NORMALKEYBG_FIELD, ColorProvider.getRandomColorDrawable());
                XposedHelpers.setObjectField(param.thisObject, FCTKEYBG_FIELD, ColorProvider.getRandomColorDrawable());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                XposedHelpers.setObjectField(param.thisObject, NORMALKEYBG_FIELD,
                        XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEBG_FIELD));
                XposedHelpers.setObjectField(param.thisObject, FCTKEYBG_FIELD,
                        XposedHelpers.getAdditionalInstanceField(param.thisObject, NATIVEFCTBG_FIELD));
            }

        });
    }

}
