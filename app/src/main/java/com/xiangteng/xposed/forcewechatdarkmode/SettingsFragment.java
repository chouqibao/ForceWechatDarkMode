package com.xiangteng.xposed.forcewechatdarkmode;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    protected static int uiMode = 0;  // 当前深色模式状态
    protected static boolean isPrefChanged = false;  // 当用户更改了设置后，若没有点击 FAB 而是直接退出则会给出提示

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference darkModeStatus = findPreference(getResources().getString(R.string.preference_dark_mode_status_key));
        switch (uiMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkModeStatus.setTitle(R.string.preference_dark_mode_status_title_on);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                darkModeStatus.setTitle(R.string.preference_dark_mode_status_title_off);
                darkModeStatus.setSummary(R.string.prompt_when_dark_mode_off);
                break;
            default:
                darkModeStatus.setTitle(R.string.preference_dark_mode_status_title_unknown);
                darkModeStatus.setSummary(R.string.prompt_when_dark_mode_off);
        }

        findPreference(getResources().getString(R.string.preference_switch_enhanced_mode_key)).setOnPreferenceChangeListener(this);
        SwitchPreference switchLogToFile = findPreference(getResources().getString(R.string.preference_switch_log_to_file_key));
        switchLogToFile.setOnPreferenceChangeListener(this);
        findPreference(getResources().getString(R.string.preference_clear_log_key)).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        isPrefChanged = true;
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getResources().getString(R.string.preference_clear_log_key))) {
            MyLog.getInstance().clearLog();
        }
        return false;
    }
}
