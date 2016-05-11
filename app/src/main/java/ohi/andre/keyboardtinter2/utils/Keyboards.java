package ohi.andre.keyboardtinter2.utils;

import android.os.Build;

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

    public static Hooker getHooker(String name) {
        int vrs = Build.VERSION.SDK_INT;
        if (name.equals(GOOGLE_KEYBOARD)) {
            if (vrs >= Build.VERSION_CODES.M)
                return new API23();
            else
                return new API21();
        }
        if (name.equals(AOSP_KEYBOARD)) {
            if (vrs >= Build.VERSION_CODES.M)
                return new API23();
            if (vrs >= Build.VERSION_CODES.LOLLIPOP)
                return new API21();
            if (vrs >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                return new API17();
            if (vrs >= Build.VERSION_CODES.JELLY_BEAN)
                return new API16();
            if (vrs >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                return new API14();
        }
        if (name.equals(SWIFTKEY_KEYBOARD) || name.equals(SWIFTKEY_NEURAL_KEYBOARD) || name.equals(SWIFTKEY_BETA_KEYBOARD))
            return new SwiftKeyHooker();
        if (name.equals(HACKERS_KEYBOARD))
            return new HackersHooker();
        if (name.equals(SMART_KEYBOARD_PRO))
            return new SmartKeyboardHooker();
        if (name.equals(XPERIA_KEYBOARD))
            return new XperiaHooker();
        if (name.equals(TOUCHPALEMOJI_KEYBOARD) || name.equals(TOUCHPAL_KEYBOARD))
            return new TouchpalHooker();
        if (name.equals(BLACKBERRY_KEYBOARD))
            return new BlackberryHooker();
        if (name.equals(IKEYBOARD))
            return new IKeyboardHooker();
        if (name.equals(SWIPE_KEYBOARD))
            return new SwipeHooker();
        if (name.equals(GO_KEYBOARD))
            return new GoHooker();
        if (name.equals(DODOL_KEYBOARD))
            return new DodolHooker();
        if (name.equals(RU_KEYBOARD))
            return new RuHooker();
        if (name.equals(ASUS_KEYBOARD))
            return new AsusHooker();
        if (name.equals(HTC_KEYBOARD))
            return new HTCHooker();
        if (name.equals(CHROOMA_KEYBOARD))
            return new ChroomaHooker();
        if (name.equals(AITYPE_KEYBOARD))
            return new AiHooker();
        if (name.equals(LG_KEYBOARD))
            return new LgHooker();
        if (name.equals(INDIC_KEYBAORD))
            return new API23();
        if (name.equals(PINYIN_KEYBOARD))
            return new API23();

        return null;
    }

}


