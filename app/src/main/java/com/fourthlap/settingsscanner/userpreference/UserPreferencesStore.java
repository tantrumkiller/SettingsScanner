package com.fourthlap.settingsscanner.userpreference;

import android.content.SharedPreferences;
import android.util.Log;
import com.fourthlap.settingsscanner.setting.Setting;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserPreferencesStore {

  public Set<Setting> getSettingsEnabledForWatch(final SharedPreferences sharedPref) {
    Map<String, ?> existingPreferences = sharedPref.getAll();

    if (existingPreferences.size() != Setting.values().length) {
      Log.i("UserPreferencesStore", "New settings introduced, updating default values");
      updateNonExistingDefaultPreferences(sharedPref, existingPreferences);
    }

    final Set<Setting> whitelistedSettings = new HashSet<>();

    for (Setting setting : Setting.values()) {
      boolean isEnabled = sharedPref.getBoolean(setting.name(), true);
      if (isEnabled) {
        whitelistedSettings.add(setting);
      }
    }

    return Collections.unmodifiableSet(whitelistedSettings);
  }

  public void updateSettingPreference(final SharedPreferences sharedPref, final Setting setting,
      boolean isEnabled) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    Log.i("UserPreferencesStore", setting + " isEnabled:" + isEnabled);
    editor.putBoolean(setting.name(), isEnabled);
    editor.commit();
  }

  private void updateNonExistingDefaultPreferences(final SharedPreferences sharedPref,
      final Map<String, ?> existingPreferences) {
    for (Setting setting : Setting.values()) {
      if (!existingPreferences.containsKey(setting.name())) {
        updateSettingPreference(sharedPref, setting, true);
      }
    }
  }
}
