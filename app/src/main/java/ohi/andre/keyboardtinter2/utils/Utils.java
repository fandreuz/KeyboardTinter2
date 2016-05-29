package ohi.andre.keyboardtinter2.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 25/10/15.
 */
public class Utils {

    private static final String EMAIL_TYPE = "vnd.android.cursor.dir/email";

    public static Intent getEmailIntent(String address, String object, File... attachments) {
        Intent emailIntent;

        if(attachments != null && attachments.length > 1) {
            emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

            ArrayList<Uri> uris = new ArrayList<>();
            for(File f : attachments) {
                uris.add(Uri.fromFile(f));
            }
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            emailIntent = new Intent(Intent.ACTION_SEND);

            if(attachments != null && attachments.length == 1) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attachments[0]));
            }
        }

        emailIntent.setType(EMAIL_TYPE);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {address});

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, object);
        return emailIntent;
    }

    public static Integer[] toIntegerArray(int[] array) {
        Integer[] integers = new Integer[array.length];
        for(int ctr = 0; ctr < array.length; ctr++) {
            integers[ctr] = array[ctr];
        }
        return integers;
    }

    public static File getAndroidLog(File storeFolder){
        File file = new File (storeFolder, "android.log");

        int pid = android.os.Process.myPid();
        try {
            String command = "logcat -d -v threadtime *:*";
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(String.valueOf(pid))) {
                    result.append(currentLine);
                    result.append("\n");
                }
            }

            FileWriter out = new FileWriter(file);
            out.write(result.toString());
            out.close();

            Runtime.getRuntime().exec("logcat -c");

        } catch (IOException e) {}

        return file;
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
