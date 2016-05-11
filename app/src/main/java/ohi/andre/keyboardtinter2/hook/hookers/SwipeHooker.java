package ohi.andre.keyboardtinter2.hook.hookers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

/**
 * Created by andre on 10/12/15.
 */
public class SwipeHooker implements Hooker {

    private static View keyboardView;
    private static Object storedKey;
    private static Drawable nativeDrawable;
    private static Canvas canvas;
    private static Paint paint;
    private static Rect padding;
    private static Rect dirtyRect;
    private static boolean check = false;
    private final String KEYBOARDVIEW_CLASS = "com.nuance.swype.input.KeyboardViewEx";
    private final String COREINPUT_CLASS = "com.nuance.input.swypecorelib.XT9CoreInput";
    private final String KEYBOARDMANAGER_CLASS = "com.nuance.swype.input.KeyboardManager";
    private final String KEYBOARDCACHE_FIELD = "keyboardCache";
    private final String KEYBOARDID_FIELD = "keyboardId";
    private final String KEYBG_FIELD = "mKeyBackground";
    private final String DIRTYRECT_FIELD = "mDirtyRect";
    private final String WIDTH_FIELD = "width";
    private final String HEIGHT_FIELD = "height";
    private final String X_FIELD = "x";
    private final String Y_FIELD = "y";
    private final String LABEL_FIELD = "label";
    private final String PRESSED_FIELD = "pressed";
    private final String KEYINDEXBYTAP_METHODNAME = "getKeyIndexByTap";
    private final String DRAWKEY_METHODNAME = "drawKey";
    private final String GET_METHODNAME = "get";
    private final String GETKEYS_METHODNAME = "getKeys";
    private final String ONTOUCHEVENT_METHODNAME = "onTouchEvent";

    @Override
    public void hook(ClassLoader loader) {
        Class<?> kView = XposedHelpers.findClass(KEYBOARDVIEW_CLASS, loader);
        Class<?> coreInput = XposedHelpers.findClass(COREINPUT_CLASS, loader);
        Class<?> kMgr = XposedHelpers.findClass(KEYBOARDMANAGER_CLASS, loader);

        Method m = null;
        for (Method method : kView.getDeclaredMethods())
            if (method.getName().equals(DRAWKEY_METHODNAME))
                m = method;
        final Method drawKey = m;
        drawKey.setAccessible(true);

        XposedBridge.hookAllConstructors(kView, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                keyboardView = (View) param.thisObject;
            }
        });

        XposedHelpers.findAndHookMethod(kMgr, KEYINDEXBYTAP_METHODNAME, coreInput, int.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        int index = (int) param.getResult();
                        Object cache = XposedHelpers.getObjectField(param.thisObject, KEYBOARDCACHE_FIELD);
                        Object kb = XposedHelpers.callMethod(cache, GET_METHODNAME, XposedHelpers.getIntField(param.thisObject, KEYBOARDID_FIELD));
                        Object touchedKey = ((List<Object>) XposedHelpers.callMethod(kb, GETKEYS_METHODNAME)).get(index);

                        if (nativeDrawable == null)
                            nativeDrawable = (Drawable) XposedHelpers.getObjectField(touchedKey, KEYBG_FIELD);

                        if (storedKey == null)
                            storedKey = touchedKey;
                        else if (!getText(storedKey).equals(getText(touchedKey))) {
                            XposedHelpers.setObjectField(storedKey, KEYBG_FIELD, nativeDrawable);
                            reDrawKey(drawKey, storedKey);

                            storedKey = touchedKey;
                        } else
                            return;

                        XposedHelpers.setObjectField(storedKey, KEYBG_FIELD, ColorProvider.getRandomColorDrawable());
                        reDrawKey(drawKey, storedKey);
                    }
                });

        XposedHelpers.findAndHookMethod(kView, ONTOUCHEVENT_METHODNAME, MotionEvent.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                MotionEvent event = (MotionEvent) param.args[0];
                if (event.getAction() == MotionEvent.ACTION_UP && storedKey != null) {
                    XposedHelpers.setObjectField(storedKey, KEYBG_FIELD, nativeDrawable);

                    StateListDrawable drawable = (StateListDrawable) XposedHelpers.getObjectField(storedKey, KEYBG_FIELD);
                    drawable.setState(new int[]{});

                    reDrawKey(drawKey, storedKey);

                    storedKey = null;
                }
            }
        });

        XposedBridge.hookMethod(drawKey, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                if (canvas == null) canvas = (Canvas) param.args[0];
                if (paint == null) paint = (Paint) param.args[1];
                if (padding == null) padding = (Rect) param.args[4];

                if (!check) return;

                dirtyRect = (Rect) XposedHelpers.getObjectField(param.thisObject, "mDirtyRect");
//
                Object key = param.args[2];
                int x = XposedHelpers.getIntField(key, X_FIELD);
                int y = XposedHelpers.getIntField(key, Y_FIELD);
                int width = XposedHelpers.getIntField(key, WIDTH_FIELD) + x;
                int height = XposedHelpers.getIntField(key, HEIGHT_FIELD) + y;
                Rect newRect = new Rect(x, y, width, height);

                XposedHelpers.setObjectField(param.thisObject, DIRTYRECT_FIELD, newRect);

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (check)
                    check = false;
                else
                    return;

                XposedHelpers.setObjectField(param.thisObject, DIRTYRECT_FIELD, dirtyRect);
            }
        });
    }

    private void reDrawKey(Method method, Object key) {
        try {
            check = true;
            setPressed(key, true);
            method.invoke(keyboardView, canvas, paint, key, true, padding, -1, 0, 0, 1f, 1f);
        } catch (Exception e) {
        }
    }

    private void setPressed(Object key, boolean pressed) {
        XposedHelpers.setBooleanField(key, PRESSED_FIELD, pressed);
    }

    private String getText(Object key) {
        Object label = XposedHelpers.getObjectField(key, LABEL_FIELD);
        return label == null ? "" : label.toString();
    }
}
