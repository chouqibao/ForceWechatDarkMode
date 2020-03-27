package com.xiangteng.xposed.forcewechatdarkmode;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

public class WeChatDarkModePreferenceProvider extends RemotePreferenceProvider {
    public WeChatDarkModePreferenceProvider() {
        super(BuildConfig.APPLICATION_ID + ".preferences", new String[]{
                BuildConfig.APPLICATION_ID + "_preferences",
        });
    }
}
