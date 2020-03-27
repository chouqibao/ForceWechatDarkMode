package com.xiangteng.xposed.forcewechatdarkmode.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.robv.android.xposed.XposedBridge;

import static android.util.Log.getStackTraceString;

public class MyLog {
    private static boolean sWriteXposedLog = false;

    public static void setXposedLog(boolean write_xposed_log) {
        sWriteXposedLog = write_xposed_log;
    }

    public static void v(@Nullable String tag, @NonNull String msg) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg);
        else Log.v(tag, msg);
    }

    public static void v(@Nullable String tag, @Nullable String msg, @Nullable Throwable tr) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg + '\n' + getStackTraceString(tr));
        else Log.v(tag, msg, tr);
    }

    public static void d(@Nullable String tag, @NonNull String msg) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg);
        else Log.d(tag, msg);
    }

    public static void d(@Nullable String tag, @Nullable String msg, @Nullable Throwable tr) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg + '\n' + getStackTraceString(tr));
        else Log.d(tag, msg, tr);
    }

    public static void i(@Nullable String tag, @NonNull String msg) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg);
        else Log.i(tag, msg);
    }

    public static void i(@Nullable String tag, @Nullable String msg, @Nullable Throwable tr) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg + '\n' + getStackTraceString(tr));
        else Log.i(tag, msg, tr);
    }

    public static void w(@Nullable String tag, @NonNull String msg) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg);
        else Log.w(tag, msg);
    }

    public static void w(@Nullable String tag, @Nullable Throwable tr) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + getStackTraceString(tr));
        else Log.w(tag, tr);
    }

    public static void w(@Nullable String tag, @Nullable String msg, @Nullable Throwable tr) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg + '\n' + getStackTraceString(tr));
        else Log.w(tag, msg, tr);
    }

    public static void e(@Nullable String tag, @NonNull String msg) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg);
        else Log.e(tag, msg);
    }

    public static void e(@Nullable String tag, @Nullable String msg, @Nullable Throwable tr) {
        if (sWriteXposedLog) XposedBridge.log(tag + ": " + msg + '\n' + getStackTraceString(tr));
        else Log.e(tag, msg, tr);
    }

    public static void wtf(@Nullable String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void wtf(@Nullable String tag, @Nullable Throwable tr) {
        Log.w(tag, tr);
    }

    public static void wtf(@Nullable String tag, @Nullable String msg, @Nullable Throwable tr) {
        Log.w(tag, msg, tr);
    }
}
