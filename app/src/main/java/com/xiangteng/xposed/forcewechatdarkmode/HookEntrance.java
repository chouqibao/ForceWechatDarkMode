package com.xiangteng.xposed.forcewechatdarkmode;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.xiangteng.xposed.forcewechatdarkmode.Constants.LOG_TAG;
import static com.xiangteng.xposed.forcewechatdarkmode.Constants.TARGET_PACKAGE_NAME;
import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookEntrance implements IXposedHookLoadPackage {

    private static ClassLoader classloader;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!TARGET_PACKAGE_NAME.equals(lpparam.packageName))
            return;

        MyLog.getInstance().setDoWriteToFile((new XSharedPreferences(new File(Constants.EXTERNAL_SHARED_PREFS_PATH))).getBoolean("preference_switch_log_to_file_key", false));
        MyLog.getInstance().i(LOG_TAG, "WeChat Hooked.");

        try {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    MyLog.getInstance().i(LOG_TAG, "attach() hooked.");
                    classloader = ((Context) param.args[0]).getClassLoader();
                    hook();
                }
            });
        } catch (Throwable t) {
            MyLog.getInstance().e(LOG_TAG, "Hook attach() error.", t);
        }
    }

    private void hook() {

        try {
            findAndHookMethod("com.tencent.mm.plugin.expt.d.b", classloader, "b", String.class, String.class, boolean.class, boolean.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String para0 = param.args[0].toString();
                    MyLog.getInstance().d(LOG_TAG, String.format("com.tencent.mm.plugin.expt.d.b.b() called, para: %s, result: %s", para0, param.getResult().toString()));
                    if (para0.equals("clicfg_dark_mode_brand_api")) {
                        MyLog.getInstance().i(LOG_TAG, "com.tencent.mm.plugin.expt.d.b.b(\"clicfg_dark_mode_brand_api\") called.");
                        param.setResult(Build.BRAND.toLowerCase() + "&" + Build.VERSION.SDK_INT);
                    } else if (para0.equals("clicfg_dark_mode_unused_on")) {
                        MyLog.getInstance().i(LOG_TAG, "com.tencent.mm.plugin.expt.d.b.b(\"clicfg_dark_mode_unused_on\") called.");
                        param.setResult("1");
                    }
                }
            });
        } catch (Throwable t) {
            MyLog.getInstance().e(LOG_TAG, "Hook com.tencent.mm.plugin.expt.d.b.b() error.", t);
        }

        try {
            hookAllMethods(classloader.loadClass("com.tencent.mmkv.MMKV"), "getBoolean", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String para0 = param.args[0].toString();
                    if (para0.equals("dark_mode_used") || para0.equals("dark_mode_follow_system")) {
                        MyLog.getInstance().i(LOG_TAG, "com.tencent.mmkv.MMKV.getBoolean(\"" + para0 + "\") called.");
                        param.setResult(true);
                    }
                }
            });
        } catch (Throwable t) {
            MyLog.getInstance().e(LOG_TAG, "Hook com.tencent.mmkv.MMKV.getBoolean() error.", t);
        }

        XSharedPreferences preferences = new XSharedPreferences(new File(Constants.EXTERNAL_SHARED_PREFS_PATH));
        Boolean is_enhanced_mode_on = preferences.getBoolean("preference_switch_enhanced_mode_key", false);
        if (is_enhanced_mode_on) {
            try {
                findAndHookMethod(Resources.class, "getConfiguration", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Configuration configuration = (Configuration) param.getResult();
                        MyLog.getInstance().i(LOG_TAG, "Resources.getConfiguration() called, uiMode: " + configuration.uiMode);
                        configuration.uiMode = Configuration.UI_MODE_NIGHT_YES;
                    }
                });
            } catch (Throwable t) {
                MyLog.getInstance().e(LOG_TAG, "Hook Resources.getConfiguration() to set uiMode error.", t);
            }
        }
    }
}
