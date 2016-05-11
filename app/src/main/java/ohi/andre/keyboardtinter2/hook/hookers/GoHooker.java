package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by francescoandreuzzi on 18/12/15.
 */
public class GoHooker implements Hooker {

    LayerDrawable nativeBackground;

    int n = 1;

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass("com.jb.gokeyboard.ui.frame.KeyboardView", loader);

        XposedHelpers.findAndHookMethod(kView, "A", new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Log.e("andre", "n: " + n);

                if (nativeBackground == null)
                    nativeBackground = (LayerDrawable) XposedHelpers.getObjectField(param.thisObject, "ar");

                Object key = XposedHelpers.getObjectField(param.thisObject, "aN");
                if (key == null)
                    return;

//                int[] state = (int[]) XposedHelpers.callMethod(key, "a");
//
//                String character = (String) XposedHelpers.getObjectField(key, "e");
//                Log.e("andre", character + ": " + Arrays.toString(state));

//                if(state == null || !Utils.containsInt(state, android.R.attr.state_pressed))
//                    XposedHelpers.setObjectField(param.thisObject, "ar", nativeBackground);
//                else
//                    XposedHelpers.setObjectField(param.thisObject, "ar", new LayerDrawable(new Drawable[] {ColorProvider.getRandomColorDrawable()}));

                if (n == 1) {
                    XposedHelpers.setObjectField(param.thisObject, "ar", new LayerDrawable(new Drawable[]{ColorProvider.getRandomColorDrawable()}));
                    n++;
                } else if (n == 2) {
                    XposedHelpers.setObjectField(param.thisObject, "ar", new LayerDrawable(new Drawable[]{ColorProvider.getRandomColorDrawable()}));
                    n++;
                } else if (n == 3) {
                    XposedHelpers.setObjectField(param.thisObject, "ar", nativeBackground);
                    n = 1;
                }

            }
        });

    }
}
