package com.fourthlap.a.settingspatroller.setting;

import android.content.Context;

public interface SettingHandler {
    boolean isEnabled(Context context);
    void openSettingsMenu(Context context);
}
