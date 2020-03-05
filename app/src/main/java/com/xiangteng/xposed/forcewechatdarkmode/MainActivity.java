package com.xiangteng.xposed.forcewechatdarkmode;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import de.robv.android.xposed.XposedBridge;

import static com.xiangteng.xposed.forcewechatdarkmode.Constants.LOG_TAG;

public class MainActivity extends AppCompatActivity {

    private int PERMISSIONS_STORAGE_CODE = 1;
    private String[] PERMISSIONS_STORAGE_ARRAY = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        SettingsFragment.uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SettingsFragment.isPrefChanged) {
                    copyConfig();
                    SettingsFragment.isPrefChanged = false;
                    goToWechatSettingPage();
                }
            }
        });
    }

    private void verifyStoragePermissions(final Activity activity) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.storagr_permission_rationale)
                            .setPositiveButton(R.string.dialog_pos_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE_ARRAY, PERMISSIONS_STORAGE_CODE);
                                }
                            })
                            .setNegativeButton(R.string.dialog_neg_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(activity, R.string.toast_storage_permission_denied, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE_ARRAY, PERMISSIONS_STORAGE_CODE);
                }
            } else {
                copyConfig();
            }
        } catch (Throwable t) {
            Log.e(LOG_TAG, "verifyStoragePermissions() error.", t);
            XposedBridge.log(LOG_TAG + ": verifyStoragePermissions() error.");
            XposedBridge.log(t);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                copyConfig();
            } else {
                Toast.makeText(this, R.string.toast_storage_permission_denied, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    // 将配置文件从内部存储空间复制到 /sdcard 以供模块读取
    private void copyConfig() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File internelSharedPrefs = new File(getFilesDir(), "../shared_prefs/" + BuildConfig.APPLICATION_ID + "_preferences.xml");
                        File externelSharedPrefs = new File(Constants.EXTERNAL_SHARED_PREFS_PATH);
                        if (!externelSharedPrefs.exists()) {
                            getExternalFilesDir(null);  // 很奇怪，如果不用这一句，则下面首次创建目录时会报错
                            externelSharedPrefs.getParentFile().mkdirs();
                            externelSharedPrefs.createNewFile();
                        }

                        FileInputStream in = new FileInputStream(internelSharedPrefs);
                        FileOutputStream out = new FileOutputStream(externelSharedPrefs);
                        byte[] buffer = new byte[1024];
                        int numByte = in.read(buffer);
                        while (numByte != -1) {
                            out.write(buffer, 0, numByte);
                            numByte = in.read(buffer);
                        }
                        in.close();
                        out.close();
                    } catch (Throwable t) {
                        Log.e(LOG_TAG, "copyConfig() error.", t);
                        XposedBridge.log(LOG_TAG + ": copyConfig() error.");
                        XposedBridge.log(t);
                    }
                }
            }).start();
        } catch (Throwable t) {
            Log.e(LOG_TAG, "copyConfig() thread error.", t);
            XposedBridge.log(LOG_TAG + ": copyConfig() thread error.");
            XposedBridge.log(t);
        }
    }

    private void goToWechatSettingPage() {  // 打开系统设置的微信应用信息界面
        Toast.makeText(this, R.string.toast_kill_wechat, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:com.tencent.mm"));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (SettingsFragment.isPrefChanged) {  // 未保存就退出则给出提示
            new AlertDialog.Builder(this)
                    .setMessage(R.string.msg_remind_not_stored)
                    .setPositiveButton(R.string.dialog_pos_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.dialog_neg_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        } else
            super.onBackPressed();
    }
}
