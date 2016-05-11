package ohi.andre.keyboardtinter2.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by andre on 25/10/15.
 */
public class Utils {

    public static boolean containsInt(int[] array, int i) {
        if (array == null)
            return false;

        for (int n : array)
            if (n == i)
                return true;
        return false;
    }

    public static Field getFieldOfType(Field[] fields, String name, Class<?> type) {
        Field r = null;
        for (Field f : fields)
            if (f.getType().equals(type) && (name == null || f.getName().equals(name)))
                r = f;
        if (r != null)
            r.setAccessible(true);
        return r;
    }

    public static Field getFieldOfType(Field[] fields, Class<?> type) {
        return getFieldOfType(fields, null, type);
    }

    public static Method getMethod(Class<?> clazz, Class<?> returnType, Class<?>[] args) {
        Method[] methods = clazz.getDeclaredMethods();

        MainLoop:
        for (Method method : methods) {
            if (method.getReturnType().equals(returnType)) {

                Class<?>[] args2 = method.getParameterTypes();
                if (args2.length != args.length)
                    continue;

                for (int count = 0; count < args.length; count++) {
                    if (!(args[count].equals(args2[count])))
                        continue MainLoop;
                }

                return method;
            }
        }

        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean listContainsObject(List<Object> list, Object object) {
        for (Object obj : list) {
            if (obj.getClass().equals(object.getClass()))
                return true;
        }
        return false;
    }

    public static void setAllTo(Object value, Object o, Class<?> toSet) {
        Class<?> clazz = o.getClass();
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        for (int count = 0; count < fields.size(); count++) {
            Field field = fields.get(count);
            if (field.getType().equals(toSet)) {
                XposedBridge.log("setted " + field.getName() + " to " + value.toString());
                field.setAccessible(true);
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    public static void printStackTrace(StackTraceElement[] elements) {
        XposedBridge.log("--- start");

        String last = null;
        boolean lastWasTabbed = false;
        for (StackTraceElement element : elements) {
            if (last == null) {
                XposedBridge.log(element.toString());
                last = element.toString();
                continue;
            }

            String current = element.toString();

            int currentFirstPoint = current.indexOf(".");
            String after = current.substring(currentFirstPoint + 1);
            int currentSecondPoint = after.indexOf(".");
            if (currentSecondPoint == -1)
                currentSecondPoint = currentFirstPoint;

            int lastFirstPoint = last.indexOf(".");
            after = last.substring(lastFirstPoint + 1);
            int lastSecondPoint = after.indexOf(".");
            if (lastSecondPoint == -1)
                lastSecondPoint = lastFirstPoint;

            if (current.substring(0, currentSecondPoint).equals(last.substring(0, lastSecondPoint))) {
                if (lastWasTabbed)
                    XposedBridge.log("    " + current);
                else
                    XposedBridge.log(current);
            } else {
                if (lastWasTabbed) {
                    XposedBridge.log(current);
                    lastWasTabbed = false;
                } else {
                    XposedBridge.log("    " + current);
                    lastWasTabbed = true;
                }
            }

            last = current;
        }

        XposedBridge.log("--- end");
        XposedBridge.log("\n");
    }

    public static void printDeclaredMethods(Class<?> c) {
        printDeclaredMethods(c, null);
    }

    public static void printDeclaredMethods(Class<?> c, Class<?> returnType) {
        XposedBridge.log("--- methods of " + c.getName());

        for (Method method : c.getDeclaredMethods()) {
            if (returnType == null || returnType.equals(method.getReturnType()))
                XposedBridge.log(method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")");
        }

        XposedBridge.log("--- end");
        XposedBridge.log("\n");
    }

    public static void printDeclaredFields(Object o) {
        printDeclaredFields(o, null);
    }

    public static void printDeclaredFields(Object o, Class<?> type) {
        Class<?> c = o.getClass();
        XposedBridge.log("--- fields of " + c.getName());

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);

            if (type == null || type.equals(field.getType())) {
                try {
                    XposedBridge.log(field.getType().getName() + " " + field.getName() + " = " + field.get(o).toString());
                } catch (IllegalAccessException e) {
                } catch (NullPointerException e) {
                    XposedBridge.log(field.getType().getName() + " " + field.getName() + " = null");
                }
            }
        }

        XposedBridge.log("--- end");
        XposedBridge.log("\n");
    }

    public static Drawable getDrawableForState(StateListDrawable drawable, int[] state) {
        try {
            Method getStateDrawableIndex = StateListDrawable.class.getMethod("getStateDrawableIndex", int[].class);
            Method getStateDrawable = StateListDrawable.class.getMethod("getStateDrawable", int.class);
            int index = (int) getStateDrawableIndex.invoke(drawable, state);
            return (Drawable) getStateDrawable.invoke(drawable, index);
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public static Bitmap view2Bitmap(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
