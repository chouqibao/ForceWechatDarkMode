package com.xiangteng.xposed.forcewechatdarkmode;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.xiangteng.xposed.forcewechatdarkmode.util.MyLog;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.xiangteng.xposed.forcewechatdarkmode.Constants.LOG_TAG;
import static com.xiangteng.xposed.forcewechatdarkmode.Constants.TARGET_PACKAGE_NAME;
import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getIntField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class HookMain implements IXposedHookLoadPackage {

    private static boolean sEnhancedMode = false;
    private static boolean sVerboseLogging = false;
    private static boolean sWriteXposedLog = false;

    private static String getPackageVersion(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> parserCls = findClass("android.content.pm.PackageParser", lpparam.classLoader);
            Object parser = parserCls.newInstance();
            File apkPath = new File(lpparam.appInfo.sourceDir);
            Object pkg = callMethod(parser, "parsePackage", apkPath, 0);
            String versionName = (String) getObjectField(pkg, "mVersionName");
            int versionCode = getIntField(pkg, "mVersionCode");
            return String.format("%s (%d)", versionName, versionCode);
        } catch (Throwable e) {
            return "(unknown)";
        }
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!TARGET_PACKAGE_NAME.equals(lpparam.packageName))
            return;

        try {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Context context = (Context) param.args[0];
                    final RemotePreferences prefs = new RemotePreferences(context, BuildConfig.APPLICATION_ID + ".preferences", BuildConfig.APPLICATION_ID + "_preferences");

                    sEnhancedMode = prefs.getBoolean("preference_enhanced_mode_key", false);
                    sVerboseLogging = prefs.getBoolean("preference_verbose_logging_key", false);
                    sWriteXposedLog = prefs.getBoolean("preference_write_xposed_log_key", false);

                    MyLog.setXposedLog(sWriteXposedLog);
                    MyLog.i(LOG_TAG, "WeChat v" + getPackageVersion(lpparam) + " found!");
                    if (sVerboseLogging) {
                        MyLog.d(LOG_TAG, "  enhance_mode = " + sEnhancedMode);
                        MyLog.d(LOG_TAG, "  verbose_logging = " + sVerboseLogging);
                        MyLog.d(LOG_TAG, "  write_xposed_log = " + sWriteXposedLog);
                    }

                    final ClassLoader loader = context.getClassLoader();

                    try {
                        findAndHookMethod("com.tencent.mm.plugin.expt.d.b", loader, "b", String.class, String.class, boolean.class, boolean.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                String para0 = param.args[0].toString();
                                if (sVerboseLogging)
                                    MyLog.d(LOG_TAG, String.format("com.tencent.mm.plugin.expt.d.b.b() called, para: %s, result: %s", para0, param.getResult().toString()));
                                if (para0.equals("clicfg_dark_mode_brand_api")) {
                                    if (sVerboseLogging)
                                        MyLog.i(LOG_TAG, "com.tencent.mm.plugin.expt.d.b.b(\"clicfg_dark_mode_brand_api\") called.");
                                    param.setResult(Build.BRAND.toLowerCase() + "&" + Build.VERSION.SDK_INT);
                                } else if (para0.equals("clicfg_dark_mode_unused_on")) {
                                    if (sVerboseLogging)
                                        MyLog.i(LOG_TAG, "com.tencent.mm.plugin.expt.d.b.b(\"clicfg_dark_mode_unused_on\") called.");
                                    param.setResult("1");
                                }
                            }
                        });
                    } catch (Throwable t) {
                        MyLog.e(LOG_TAG, "Hook com.tencent.mm.plugin.expt.d.b.b() error.", t);
                    }

                    try {
                        hookAllMethods(loader.loadClass("com.tencent.mmkv.MMKV"), "getBoolean", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                String para0 = param.args[0].toString();
                                if (para0.equals("dark_mode_used") || para0.equals("dark_mode_follow_system")) {
                                    if (sVerboseLogging)
                                        MyLog.i(LOG_TAG, "com.tencent.mmkv.MMKV.getBoolean(\"" + para0 + "\") called.");
                                    param.setResult(true);
                                }
                            }
                        });
                    } catch (Throwable t) {
                        MyLog.e(LOG_TAG, "Hook com.tencent.mmkv.MMKV.getBoolean() error.", t);
                    }

                    if (sEnhancedMode) {
                        try {
                            findAndHookMethod(Resources.class, "getConfiguration", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    Configuration configuration = (Configuration) param.getResult();
                                    if (sVerboseLogging)
                                        MyLog.i(LOG_TAG, "Resources.getConfiguration() called, uiMode: " + configuration.uiMode);
                                    configuration.uiMode = Configuration.UI_MODE_NIGHT_YES;
                                }
                            });
                        } catch (Throwable t) {
                            MyLog.e(LOG_TAG, "Hook Resources.getConfiguration() to set uiMode error.", t);
                        }
                    }
                }
            });
        } catch (Throwable t) {
            MyLog.e(LOG_TAG, "Hook attach() error.", t);
        }
    }
}
