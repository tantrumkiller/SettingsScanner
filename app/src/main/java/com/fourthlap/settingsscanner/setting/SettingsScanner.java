package com.fourthlap.settingsscanner.setting;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.scheduler.ScanScheduler;
import com.fourthlap.settingsscanner.scheduler.ScanTimeCalculator;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SettingsScanner {
  private final UserPreferences userPreferences;
  private final SettingsConfiguration settingsConfiguration;

  public SettingsScanner(UserPreferences userPreferences,
      SettingsConfiguration settingsConfiguration) {
    this.userPreferences = userPreferences;
    this.settingsConfiguration = settingsConfiguration;
  }

  public List<Setting> getEnabledSettings(final Context context) {
    final List<Setting> enabledSettings = new ArrayList<>();
    for (final Setting setting : getSettingsToBeScanned(context)) {
      if (settingsConfiguration.getHandler(setting).isEnabled(context)) {
        enabledSettings.add(setting);
      }
    }

    return Collections.unmodifiableList(enabledSettings);
  }

  public boolean isAnySettingEnabled(final Context context) {
    for (final Setting setting : getSettingsToBeScanned(context)) {
      if (settingsConfiguration.getHandler(setting).isEnabled(context)) {
        return true;
      }
    }

    return false;
  }

  private Set<Setting> getSettingsToBeScanned(final Context context) {
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);
    return userPreferences
        .getSettingsToBeScanned(sharedPreferences);
  }
}
