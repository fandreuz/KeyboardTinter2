package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.ColorFilter;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

public class SwiftKeyHooker implements Hooker {

    private final String CFCNT_CLASS = "com.touchtype.keyboard.h.d.f$b";
    private final String CFCNT_CLASS_B = "com.touchtype.keyboard.theme.util.e$b";
    private final String CFCNT_CLASS_C = "com.touchtype.keyboard.g.d.e$b";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> util;
        try {
            util = loader.loadClass(CFCNT_CLASS);
        } catch (ClassNotFoundException e) {
            try {
                util = loader.loadClass(CFCNT_CLASS_B);
            } catch (ClassNotFoundException e1) {
                try {
                    util = loader.loadClass(CFCNT_CLASS_C);
                } catch (ClassNotFoundException e2) {
//					amen
                    return;
                }
            }
        }

        Method m = XposedHelpers.findMethodsByExactParameters(util, ColorFilter.class, int[].class)[0];
        XposedBridge.hookMethod(m, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param)
                    throws Throwable {
                super.afterHookedMethod(param);

                int[] arg = (int[]) param.args[0];

                if (arg == null) {
                    XposedBridge.log("null");
                    return;
                }

                if (arg.length == 0)
                    return;

                param.setResult(ColorProvider.getRandomColorFilter());
            }
        });
    }

}


