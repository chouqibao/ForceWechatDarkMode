package com.xiangteng.xposed.forcewechatdarkmode;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import com.xiangteng.xposed.forcewechatdarkmode.util.MyLog;

import androidx.annotation.Keep;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import static com.xiangteng.xposed.forcewechatdarkmode.Constants.LOG_TAG;

public class SettingsFragment extends PreferenceFragmentCompat {

    public interface Callback {
        void onPreferenceChanged(String key);
    }

    private Callback getCallback() {
        return (Callback) requireActivity();
    }

    @Keep
    public static Integer getActiveXposedVersion() {
        MyLog.d(LOG_TAG, "ForceWeChatDarkMode is not active!");
        return -1;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference xposed_version = findPreference("preference_xposed_version_key");
        Preference dark_mode_status = findPreference("preference_dark_mode_status_key");
        SwitchPreference hide_launcher_icon = findPreference("preference_hide_launcher_icon_key");
        SwitchPreference enhanced_mode = findPreference("preference_enhanced_mode_key");
        SwitchPreference verbose_logging = findPreference("preference_verbose_logging_key");
        SwitchPreference write_xposed_log = findPreference("preference_write_xposed_log_key");

        hide_launcher_icon.setOnPreferenceChangeListener((pref, newValue) -> {
            final PackageManager p = requireContext().getPackageManager();
            final ComponentName componentName = new ComponentName(requireContext(), BuildConfig.APPLICATION_ID + ".MainActivityLauncher");
            p.setComponentEnabledSetting(
                    componentName,
                    (boolean) newValue ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            return true;
        });

        final Preference.OnPreferenceChangeListener listener = (preference, newValue) -> {
            getCallback().onPreferenceChanged(preference.getKey());
            return true;
        };

        enhanced_mode.setOnPreferenceChangeListener(listener);
        verbose_logging.setOnPreferenceChangeListener(listener);
        write_xposed_log.setOnPreferenceChangeListener(listener);

        if (getActiveXposedVersion() == -1) {
            dark_mode_status.setEnabled(false);
            hide_launcher_icon.setEnabled(false);
            enhanced_mode.setEnabled(false);
            verbose_logging.setEnabled(false);
            write_xposed_log.setEnabled(false);
        } else {
            xposed_version.setTitle(getString(R.string.preference_xposed_version_title, getActiveXposedVersion()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Preference dark_mode_status = findPreference("preference_dark_mode_status_key");
        int ui_mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (ui_mode) {
            case Configuration.UI_MODE_NIGHT_YES:
                dark_mode_status.setTitle(R.string.preference_dark_mode_status_title_on);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                dark_mode_status.setTitle(R.string.preference_dark_mode_status_title_off);
                dark_mode_status.setSummary(R.string.prompt_when_dark_mode_off);
                break;
            default:
                dark_mode_status.setTitle(R.string.preference_dark_mode_status_title_unknown);
                dark_mode_status.setSummary(R.string.prompt_when_dark_mode_off);
        }
    }

    //    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//        switch (preference.getKey()) {
//            case "preference_switch_enhanced_mode_key":
//            case "preference_write_xposed_log_key":
////                mIsPrefChanged = true;
//                break;
//            case "preference_hide_launcher_icon_key":
//                boolean value = (boolean) newValue;
//                if (value) {
//
//                } else {
//
//                }
//                break;
//        }
//        return true;
//    }
}
