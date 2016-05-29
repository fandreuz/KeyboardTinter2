package ohi.andre.keyboardtinter2.hook.hookers.aosp;

/*Copyright Francesco Andreuzzi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import ohi.andre.keyboardtinter2.hook.Hooker;
import ohi.andre.keyboardtinter2.utils.ColorProvider;

public class API23 implements Hooker {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void hook(ClassLoader loader) {
        final Class<?> keyClass = XposedHelpers.findClass("com.google.android.apps.inputmethod.libs.framework.keyboard.SoftKeyView", loader);

        XposedHelpers.findAndHookMethod(keyClass, "onTouchEvent", MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                final ViewGroup group = (ViewGroup) param.thisObject;
                group.setBackgroundColor(ColorProvider.getRandomColor());

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        try {
                            sleep(100);
                        } catch (InterruptedException e) {}

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                group.setBackgroundDrawable(null);
                            }
                        });
                    }
                }.start();
            }
        });
    }
}
