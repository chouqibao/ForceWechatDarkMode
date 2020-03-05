package com.xiangteng.xposed.forcewechatdarkmode;

import android.os.Environment;

public class Constants {
    public static final String LOG_TAG = "FWDM";
    public static final String EXTERNAL_STORAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + BuildConfig.APPLICATION_ID + "/";
    public static final String EXTERNAL_SHARED_PREFS_PATH = EXTERNAL_STORAGE_DIR + "config/preferences.xml";
    public static final String LOG_FILE_NAME = EXTERNAL_STORAGE_DIR + "log/log.txt";
    public static final String TARGET_PACKAGE_NAME = "com.tencent.mm";
}
