package com.xiangteng.xposed.forcewechatdarkmode;

import com.xiangteng.xposed.forcewechatdarkmode.util.MyLog;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.xiangteng.xposed.forcewechatdarkmode.BuildConfig.APPLICATION_ID;
import static com.xiangteng.xposed.forcewechatdarkmode.Constants.LOG_TAG;
import static de.robv.android.xposed.XC_MethodReplacement.returnConstant;
import static de.robv.android.xposed.XposedBridge.getXposedVersion;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookSelf implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!APPLICATION_ID.equals(lpparam.packageName))
            return;

        MyLog.d(LOG_TAG, "Hooking " + lpparam.packageName + ", ver = " + getXposedVersion());
        findAndHookMethod(SettingsFragment.class.getName(), lpparam.classLoader, "getActiveXposedVersion", returnConstant(getXposedVersion()));
    }

}
