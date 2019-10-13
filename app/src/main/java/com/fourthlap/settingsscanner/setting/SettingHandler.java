package com.fourthlap.settingsscanner.setting;

import android.content.Context;

public interface SettingHandler {

  boolean isEnabled(Context context);

  void openSettingsMenu(Context context);
}
