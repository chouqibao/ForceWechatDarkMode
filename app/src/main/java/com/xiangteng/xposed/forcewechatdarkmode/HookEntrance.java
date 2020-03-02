package com.xiangteng.xposed.forcewechatdarkmode;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookEntrance implements IXposedHookLoadPackage {

    private final static String LOG_TAG = "FWDM";
    private final static String target_package = "com.tencent.mm";
    private static ClassLoader classloader;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!target_package.equals(lpparam.packageName))
            return;
        Log.i(LOG_TAG, "WeChat hooked.");
        try {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "attach() hooked.");
                    classloader = ((Context) param.args[0]).getClassLoader();
                    hook();
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook attach() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    private void hook() {
        /*try {
            findAndHookMethod(Activity.class, "setTheme", int.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, String.format("setTheme() called, caller: %s, para: %s.", param.thisObject.toString(), param.args[0].toString()));
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook setTheme() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        try {
            findAndHookMethod("com.tencent.mm.ui.af", classloader, "k", Resources.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.ui.af.k() called.");
                    param.setResult(true);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.ui.af.k() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        try {
            findAndHookMethod("com.tencent.mm.ui.af", classloader, "dtA", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.ui.af.dtA() called.");
                    param.setResult(true);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.ui.af.dtA() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }*/

        // 非 Play 版微信需 hook 此方法
        try {
            findAndHookMethod("com.tencent.mm.ui.af", classloader, "fqj", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.ui.af.fqj() called, result: " + param.getResult());
                    param.setResult(true);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.ui.af.fqj() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        // Play 版微信需 hook 此方法
        try {
            findAndHookMethod("com.tencent.mm.ui.af", classloader, "fnH", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.ui.af.fnH() called, result: " + param.getResult());
                    param.setResult(true);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.ui.af.fnH() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        try {
            findAndHookMethod("com.tencent.mm.ui.af", classloader, "fgx", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.ui.af.fgx() called, result: " + param.getResult());
                    param.setResult(true);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.ui.af.fgx() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        try {
            findAndHookMethod("com.tencent.mm.ui.af", classloader, "fgw", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.ui.af.fgw() called, result: " + param.getResult());
                    param.setResult(true);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.ui.af.fgw() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
    }
}
