package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;
import ohi.andre.keyboardtinter2.utils.Utils;

/**
 * Created by francescoandreuzzi on 03/02/16.
 */
public class AiHooker implements Hooker {

    private final String KEY_CLASS = "com.aitype.tablet.AItypeKey";
    private final String KVIEW_CLASS = "com.android.inputmethod.latin.LatinKeyboardBaseView";

    private final String DRAWABLESTATE_METHODNAME = "getPressedDrawableState";

    private Drawable nativeDrawable;

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass(KVIEW_CLASS, loader);
        Class<?> key = XposedHelpers.findClass(KEY_CLASS, loader);

//        XposedHelpers.findAndHookMethod(kView, "a", Canvas.class, Paint.class, int.class, int.class, key, boolean.class, float.class,
//                float.class, new XC_MethodHook() {
//
//                    @Override
//                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
//                        super.beforeHookedMethod(param);
//
//                        Object key = param.args[4];
//                        if(key == null)
//                            return;
//
//                        if(nativeDrawable == null)
//                            nativeDrawable = (Drawable) XposedHelpers.getObjectField(key, "e");
//
//                        if(Utils.containsInt((int[]) XposedHelpers.callMethod(key, DRAWABLESTATE_METHODNAME), android.R.attr.state_pressed)) {
//                            XposedHelpers.setObjectField(key, "e", ColorProvider.getRandomColorDrawable());
//                            XposedBridge.log("ohi");
//                        }
//                        else
//                            XposedHelpers.setObjectField(key, "e", nativeDrawable);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
//
//                        Object key = param.args[4];
//                        if(key == null)
//                            return;
//
//                        XposedHelpers.setObjectField(key, "e", nativeDrawable);
//                    }
//                });
//
//        XposedHelpers.findAndHookMethod(key, "onPressed", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//                if(nativeDrawable == null)
//                    nativeDrawable = (Drawable) XposedHelpers.getObjectField(param.thisObject, "e");
//
//                XposedHelpers.setObjectField(param.thisObject, "e", ColorProvider.getRandomColorDrawable());
//
//                Utils.printStackTrace(Thread.currentThread().getStackTrace());
//            }
//        });
//
//        XposedHelpers.findAndHookMethod(key, "onReleased", boolean.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//                if(nativeDrawable == null)
//                    nativeDrawable = (Drawable) XposedHelpers.getObjectField(param.thisObject, "e");
//
//                XposedHelpers.setObjectField(param.thisObject, "e", nativeDrawable);
//            }
//        });

//        Method method = XposedHelpers.findMethodsByExactParameters(kView, boolean.class, MotionEvent.class)[0];
//        XposedBridge.hookMethod(method, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//                XposedBridge.log("called");
//            }
//        });

        XposedHelpers.findAndHookMethod(kView, "a", MotionEvent.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Object tw = XposedHelpers.getObjectField(param.thisObject, "aN");

                Bitmap bmp = Utils.drawableToBitmap(ColorProvider.getRandomColorDrawable());
                BitmapDrawable drawable = new BitmapDrawable(null, bmp);

                for (Field field : tw.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getType().equals(Drawable.class)) {
                        field.set(tw, drawable);
                        XposedBridge.log("done");
                    }
                }
            }
        });
    }
}
