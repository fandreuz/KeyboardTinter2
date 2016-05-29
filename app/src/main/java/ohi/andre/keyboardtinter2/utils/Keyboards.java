package ohi.andre.keyboardtinter2.utils;

import android.os.Build;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.hook.hookers.AiHooker;
import ohi.andre.keyboardtinter2.hook.hookers.AsusHooker;
import ohi.andre.keyboardtinter2.hook.hookers.BlackberryHooker;
import ohi.andre.keyboardtinter2.hook.hookers.ChroomaHooker;
import ohi.andre.keyboardtinter2.hook.hookers.DodolHooker;
import ohi.andre.keyboardtinter2.hook.hookers.GoHooker;
import ohi.andre.keyboardtinter2.hook.hookers.HTCHooker;
import ohi.andre.keyboardtinter2.hook.hookers.HackersHooker;
import ohi.andre.keyboardtinter2.hook.hookers.IKeyboardHooker;
import ohi.andre.keyboardtinter2.hook.hookers.LgHooker;
import ohi.andre.keyboardtinter2.hook.hookers.RuHooker;
import ohi.andre.keyboardtinter2.hook.hookers.SmartKeyboardHooker;
import ohi.andre.keyboardtinter2.hook.hookers.SwiftKeyHooker;
import ohi.andre.keyboardtinter2.hook.hookers.SwipeHooker;
import ohi.andre.keyboardtinter2.hook.hookers.TouchpalHooker;
import ohi.andre.keyboardtinter2.hook.hookers.XperiaHooker;
import ohi.andre.keyboardtinter2.hook.hookers.aosp.API14;
import ohi.andre.keyboardtinter2.hook.hookers.aosp.API16;
import ohi.andre.keyboardtinter2.hook.hookers.aosp.API17;
import ohi.andre.keyboardtinter2.hook.hookers.aosp.API21;
import ohi.andre.keyboardtinter2.hook.hookers.aosp.API23;

public class Keyboards {

    private static Map<String, Class<? extends Hooker>> keyboards = new HashMap<>();

    private static final String GOOGLE_KEYBOARD = "com.google.android.inputmethod.latin";
    private static final String AOSP_KEYBOARD = "com.android.inputmethod.latin";
    private static final String SWIFTKEY_KEYBOARD = "com.touchtype.swiftkey";
    private static final String SWIFTKEY_NEURAL_KEYBOARD = "com.touchtype.swiftkey.nn";
    private static final String SWIFTKEY_BETA_KEYBOARD = "com.touchtype.swiftkey.beta";
    private static final String HACKERS_KEYBOARD = "org.pocketworkstation.pckeyboard";
    private static final String SMART_KEYBOARD_PRO = "net.cdeguet.smartkeyboardpro";
    private static final String XPERIA_KEYBOARD = "com.sonyericsson.textinput.uxp";
    private static final String TOUCHPAL_KEYBOARD = "com.cootek.smartinputv5";
    private static final String TOUCHPALEMOJI_KEYBOARD = "com.emoji.keyboard.touchpal";
    private static final String BLACKBERRY_KEYBOARD = "com.blackberry.keyboard";
    private static final String IKEYBOARD = "com.emoji.ikeyboard";
    private static final String SWIPE_KEYBOARD = "com.nuance.swype.dtc";
    private static final String GO_KEYBOARD = "com.jb.emoji.gokeyboard";
    private static final String DODOL_KEYBOARD = "com.fiberthemax.OpQ2keyboard";
    private static final String RU_KEYBOARD = "ru.androidteam.rukeyboard";
    private static final String ASUS_KEYBOARD = "com.asus.ime";
    private static final String HTC_KEYBOARD = "com.htc.sense.ime";
    private static final String CHROOMA_KEYBOARD = "com.gamelounge.chroomakeyboard";
    private static final String AITYPE_KEYBOARD = "com.aitype.android";
    private static final String LG_KEYBOARD = "com.lge.ime";
    //    private static final String MINUM_KEYBOARD = "com.whirlscape.minuumkeyboard";
    private static final String INDIC_KEYBAORD = "com.google.android.apps.inputmethod.hindi";
    private static final String PINYIN_KEYBOARD = "com.google.android.inputmethod.pinyin";

    static {
        keyboards.put(SWIFTKEY_BETA_KEYBOARD, SwiftKeyHooker.class);
        keyboards.put(SWIFTKEY_KEYBOARD, SwiftKeyHooker.class);
        keyboards.put(SWIFTKEY_NEURAL_KEYBOARD, SwiftKeyHooker.class);
        keyboards.put(HACKERS_KEYBOARD, HackersHooker.class);
        keyboards.put(SMART_KEYBOARD_PRO, SmartKeyboardHooker.class);
        keyboards.put(XPERIA_KEYBOARD, XperiaHooker.class);
        keyboards.put(TOUCHPALEMOJI_KEYBOARD, TouchpalHooker.class);
        keyboards.put(TOUCHPAL_KEYBOARD, TouchpalHooker.class);
        keyboards.put(BLACKBERRY_KEYBOARD, BlackberryHooker.class);
        keyboards.put(IKEYBOARD, IKeyboardHooker.class);
        keyboards.put(SWIPE_KEYBOARD, SwipeHooker.class);
        keyboards.put(GO_KEYBOARD, GoHooker.class);
        keyboards.put(DODOL_KEYBOARD, DodolHooker.class);
        keyboards.put(RU_KEYBOARD, RuHooker.class);
        keyboards.put(ASUS_KEYBOARD, AsusHooker.class);
        keyboards.put(HTC_KEYBOARD, HTCHooker.class);
        keyboards.put(CHROOMA_KEYBOARD, ChroomaHooker.class);
        keyboards.put(AITYPE_KEYBOARD, AiHooker.class);
        keyboards.put(LG_KEYBOARD, LgHooker.class);
        keyboards.put(PINYIN_KEYBOARD, API23.class);
        keyboards.put(INDIC_KEYBAORD, API23.class);

        int vrs = Build.VERSION.SDK_INT;
        keyboards.put(GOOGLE_KEYBOARD, vrs >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? API23.class : API21.class);
        if (vrs >= Build.VERSION_CODES.M) {
            keyboards.put(AOSP_KEYBOARD, API23.class);
        } else if (vrs >= Build.VERSION_CODES.LOLLIPOP) {
            keyboards.put(AOSP_KEYBOARD, API21.class);
        } else if (vrs >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            keyboards.put(AOSP_KEYBOARD, API17.class);
        } else if (vrs >= Build.VERSION_CODES.JELLY_BEAN) {
            keyboards.put(AOSP_KEYBOARD, API16.class);
        } else if (vrs >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            keyboards.put(AOSP_KEYBOARD, API14.class);
        }
    }

    public static Hooker getHooker(String name) {
        try {
            Constructor<? extends Hooker> constructor = keyboards.get(name).getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static boolean isSupported(String name) {
        return keyboards.containsKey(name);
    }

}


