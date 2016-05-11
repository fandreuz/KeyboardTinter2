package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;

/**
 * Created by francescoandreuzzi on 09/01/16.
 */
public class DodolHooker implements Hooker {

    final int[] single_pressed = new int[]{
            android.R.attr.state_single,
            android.R.attr.state_pressed
    };
    final int[] checked_checkable_enabled_pressed = new int[]{
            android.R.attr.state_checked,
            android.R.attr.state_checkable,
            android.R.attr.state_enabled,
            android.R.attr.state_pressed
    };
    final int[] enabled_checkable_pressed = new int[]{
            android.R.attr.state_checkable,
            android.R.attr.state_enabled,
            android.R.attr.state_pressed
    };
    final int[] enabled_pressed = new int[]{
            android.R.attr.state_enabled,
            android.R.attr.state_pressed
    };

    final int[] single = new int[]{
            android.R.attr.state_single
    };
    final int[] enabled_checked_checkable = new int[]{
            android.R.attr.state_enabled,
            android.R.attr.state_checkable,
            android.R.attr.state_checked
    };
    final int[] enabled_checkable = new int[]{
            android.R.attr.state_enabled,
            android.R.attr.state_checkable
    };
    final int[] enabled = new int[]{
            android.R.attr.state_enabled
    };

    Drawable normalDrawable = null;
    Drawable functDrawable = null;

    @Override
    public void hook(ClassLoader loader) {
        Class<?> baseView = XposedHelpers.findClass("com.fiberthemax.OpQ2keyboard.KeyboardBaseView", loader);

        XposedBridge.hookAllConstructors(baseView, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                StateListDrawable drawable = (StateListDrawable) XposedHelpers.getObjectField(param.thisObject, "mKeyBackground");
                normalDrawable = Utils.getDrawableForState(drawable, enabled);
                functDrawable = Utils.getDrawableForState(drawable, single);
            }
        });

        XposedHelpers.findAndHookMethod(baseView, "onBufferDraw", Canvas.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Object invalidatedKey = XposedHelpers.getObjectField(param.thisObject, "mInvalidatedKey");
                if (invalidatedKey == null || !XposedHelpers.getBooleanField(invalidatedKey, "pressed"))
                    return;

                StateListDrawable drawable = new StateListDrawable();

                Drawable tint = ColorProvider.getRandomColorDrawable();
                drawable.addState(single_pressed, tint);
                drawable.addState(checked_checkable_enabled_pressed, tint);
                drawable.addState(enabled_checkable_pressed, tint);
                drawable.addState(enabled_pressed, tint);

                drawable.addState(single, functDrawable);
                drawable.addState(enabled_checked_checkable, normalDrawable);
                drawable.addState(enabled_checkable, normalDrawable);
                drawable.addState(enabled, normalDrawable);

                XposedHelpers.setObjectField(param.thisObject, "mKeyBackground", drawable);
            }

        });
    }
}
