package com.example.kshitiz.helloworld.setting;

import android.content.Context;

public interface SettingHandler {
    boolean isEnabled(Context context);
    void openSettingsMenu(Context context);
}
