package com.xiangteng.xposed.forcewechatdarkmode;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;

import static com.xiangteng.xposed.forcewechatdarkmode.Constants.LOG_TAG;
import static com.xiangteng.xposed.forcewechatdarkmode.BuildConfig.DEBUG;

public class MyLog {

    private static final MyLog myLog = new MyLog();
    private static final int MAX_LENGTH = 3500;
    private boolean doWriteToFile = false;  // 是否写入到文件

    private MyLog() {
    }

    public static MyLog getInstance() {
        return myLog;
    }

    public boolean getDoWriteToFile() {
        return this.doWriteToFile;
    }

    public void setDoWriteToFile(boolean newDoWriteToFile) {
        this.doWriteToFile = newDoWriteToFile;
    }

    public void v(String tag, String message) {
        if (doWriteToFile)
            writeLog("V", tag, message);
        if (DEBUG)
            vToLogcat(tag, message);
    }

    private void vToLogcat(String tag, String message) {
        if (message.length() <= MAX_LENGTH)
            Log.v(tag, message);
        else {
            Log.v(tag, message.substring(0, MAX_LENGTH));
            vToLogcat(tag, message.substring(MAX_LENGTH));
        }
    }

    public void d(String tag, String message) {
        if (doWriteToFile)
            writeLog("D", tag, message);
        if (DEBUG)
            dToLogcat(tag, message);
    }

    private void dToLogcat(String tag, String message) {
        if (message.length() <= MAX_LENGTH)
            Log.d(tag, message);
        else {
            Log.d(tag, message.substring(0, MAX_LENGTH));
            dToLogcat(tag, message.substring(MAX_LENGTH));
        }
    }

    public void i(String tag, String message) {
        if (doWriteToFile)
            writeLog("I", tag, message);
        if (DEBUG)
            iToLogcat(tag, message);
    }

    private void iToLogcat(String tag, String message) {
        if (message.length() <= MAX_LENGTH)
            Log.i(tag, message);
        else {
            Log.i(tag, message.substring(0, MAX_LENGTH));
            iToLogcat(tag, message.substring(MAX_LENGTH));
        }
    }


    public void w(String tag, String message) {
        if (doWriteToFile)
            writeLog("W", tag, message);
        if (DEBUG)
            wToLogcat(tag, message);
    }

    private void wToLogcat(String tag, String message) {
        if (message.length() <= MAX_LENGTH)
            Log.w(tag, message);
        else {
            Log.w(tag, message.substring(0, MAX_LENGTH));
            wToLogcat(tag, message.substring(MAX_LENGTH));
        }
    }

    public void e(String tag, String message) {
        if (doWriteToFile)
            writeLog("E", tag, message);
        if (DEBUG) {
            eToLogcat(tag, message);
            XposedBridge.log(tag + ": " + message);
        }
    }

    private void eToLogcat(String tag, String message) {
        if (message.length() <= MAX_LENGTH)
            Log.e(tag, message);
        else {
            Log.e(tag, message.substring(0, MAX_LENGTH));
            eToLogcat(tag, message.substring(MAX_LENGTH));
        }
    }

    public void e(String tag, String message, Throwable t) {
        if (doWriteToFile) {
            writeLog("E", tag, message);
            writeLog("E", tag, Log.getStackTraceString(t));
        }
        if (DEBUG) {
            eToLogcat(tag, message);
            eToLogcat(tag, Log.getStackTraceString(t));
            XposedBridge.log(tag + ": " + message);
            XposedBridge.log(t);
        }
    }

    private void writeLog(final String level, final String tag, final String message) {
        File logFile = new File(Constants.LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Create log file error.");
                Log.e(LOG_TAG, Log.getStackTraceString(t));
                XposedBridge.log(LOG_TAG + ": Create log file error.");
                XposedBridge.log(t);
                return;
            }
        }
        String time = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(new Date());
        String content = String.format("%s %s/%s: %s\n", time, level, tag, message);
        FileWriter writer = null;
        try {
            writer = new FileWriter(Constants.LOG_FILE_NAME, true);
            writer.write(content);
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Write to log file error.");
            Log.e(LOG_TAG, Log.getStackTraceString(t));
            XposedBridge.log(LOG_TAG + ": Write to log file error.");
            XposedBridge.log(t);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Close writer error.");
                Log.e(LOG_TAG, Log.getStackTraceString(t));
                XposedBridge.log(LOG_TAG + ": Close writer error.");
                XposedBridge.log(t);
            }
        }
    }

    public void clearLog() {
        try {
            File logFile = new File(Constants.LOG_FILE_NAME);
            if (logFile.exists()) {
                logFile.delete();
            }
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Clear log error");
            Log.e(LOG_TAG, Log.getStackTraceString(t));
            XposedBridge.log(LOG_TAG + ": Clear log error");
            XposedBridge.log(t);
        }
    }
}
