package com.xiangteng.xposed.forcewechatdarkmode;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SettingsFragment.Callback {

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener((view) -> {
            mFab.hide();
            goToWechatSettingPage();
        });
    }

    @Override
    public void onPreferenceChanged(String key) {
        mFab.show();
    }

    private void goToWechatSettingPage() {  // 打开系统设置的微信应用信息界面
        Toast.makeText(this, R.string.toast_kill_wechat, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:com.tencent.mm"));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mFab.getVisibility() == View.VISIBLE) {  // 未保存就退出则给出提示
            new AlertDialog.Builder(this)
                    .setMessage(R.string.msg_remind_not_stored)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> MainActivity.super.onBackPressed())
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    }).show();
        } else
            super.onBackPressed();
    }
}
