package ohi.andre.keyboardtinter2.hook.hookers;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by andre on 25/10/15.
 */
public class XperiaHooker implements Hooker {

    private final String KEYBOARDVIEW_CLASS = "com.sonyericsson.textinput.uxp.view.keyboard.KeyboardView";
    private final String KEYSTYLE_CLASS = "com.sonyericsson.textinput.uxp.model.keyboard.KeyBase.KeyStyle";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> keyStyleClass = XposedHelpers.findClass(KEYSTYLE_CLASS, loader);
        Class<?> keyboardViewClass = XposedHelpers.findClass(KEYBOARDVIEW_CLASS, loader);

        XposedHelpers.findAndHookMethod(keyboardViewClass, "getKeyBackgroundPressed", keyStyleClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                param.setResult(ColorProvider.getRandomColorDrawable());
            }
        });
    }
}
