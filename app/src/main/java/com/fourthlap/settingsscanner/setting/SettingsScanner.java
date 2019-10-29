package com.fourthlap.settingsscanner.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SettingsScanner {
  private final UserPreferencesStore userPreferencesStore;
  private final SettingsConfig settingsConfig;

  public SettingsScanner(final UserPreferencesStore userPreferencesStore,
      final SettingsConfig settingsConfig) {
    this.userPreferencesStore = userPreferencesStore;
    this.settingsConfig = settingsConfig;
  }

  public List<Setting> getEnabledSettings(final Context context) {
    final List<Setting> enabledSettings = new ArrayList<>();
    for (final Setting setting : getSettingsToBeScanned(context)) {
      if (settingsConfig.getHandler(setting).isEnabled(context)) {
        enabledSettings.add(setting);
      }
    }

    return Collections.unmodifiableList(enabledSettings);
  }

  public boolean isAnySettingEnabled(final Context context) {
    for (final Setting setting : getSettingsToBeScanned(context)) {
      if (settingsConfig.getHandler(setting).isEnabled(context)) {
        return true;
      }
    }

    return false;
  }

  private Set<Setting> getSettingsToBeScanned(final Context context) {
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);
    return userPreferencesStore
        .getSettingsToBeScanned(sharedPreferences);
  }
}
