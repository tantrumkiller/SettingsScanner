package com.fourthlap.settingsscanner.userpreference;

import android.content.SharedPreferences;
import android.util.Log;
import com.fourthlap.settingsscanner.setting.Setting;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserPreferences {

  public Set<Setting> getSettingsToBeScanned(final SharedPreferences sharedPref) {
    final Set<Setting> settingsToBeScanned = new HashSet<>();

    for (Setting setting : Setting.values()) {
      boolean hasUserWhiteListed = sharedPref.getBoolean(setting.name(), true);
      if (hasUserWhiteListed) {
        settingsToBeScanned.add(setting);
      }
    }

    return Collections.unmodifiableSet(settingsToBeScanned);
  }

  public void updateSettingsToBeScannedPreferences(final SharedPreferences sharedPref,
      final Setting setting,
      boolean isWhitelistedForScan) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    Log.i("UserPreferences", setting + " isWhitelistedForScan: " + isWhitelistedForScan);
    editor.putBoolean(setting.name(), isWhitelistedForScan);
    editor.commit();
  }

  public void setSleepWindowStartTime(final SharedPreferences sharedPref, int hour, int minutes) {
    validateTimeOfTheDay(hour, minutes);

    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(UserPreferenceConstants.SLEEP_WINDOW_START_HOUR_KEY, hour);
    editor.putInt(UserPreferenceConstants.SLEEP_WINDOW_START_MIN_KEY, minutes);
    editor.commit();
  }

  public TimeOfTheDay getSleepWindowStartTime(final SharedPreferences sharedPref) {
    int hour = sharedPref.getInt(UserPreferenceConstants.SLEEP_WINDOW_START_HOUR_KEY,
        UserPreferenceConstants.DEFAULT_SLEEP_WINDOW_START.getHour());
    int minutes = sharedPref
        .getInt(UserPreferenceConstants.SLEEP_WINDOW_START_MIN_KEY,
            UserPreferenceConstants.DEFAULT_SLEEP_WINDOW_START.getMinutes());

    try {
      return new TimeOfTheDay(hour, minutes);
    } catch (IllegalArgumentException e) {
      return UserPreferenceConstants.DEFAULT_SLEEP_WINDOW_START;
    }
  }

  public void setSleepWindowEndTime(final SharedPreferences sharedPref, int hour, int minutes) {
    validateTimeOfTheDay(hour, minutes);

    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(UserPreferenceConstants.SLEEP_WINDOW_END_HOUR_KEY, hour);
    editor.putInt(UserPreferenceConstants.SLEEP_WINDOW_END_MIN_KEY, minutes);
    editor.commit();
  }

  public TimeOfTheDay getSleepWindowEndTime(final SharedPreferences sharedPref) {
    int hour = sharedPref.getInt(UserPreferenceConstants.SLEEP_WINDOW_END_HOUR_KEY,
        UserPreferenceConstants.DEFAULT_SLEEP_WINDOW_END.getHour());
    int minutes = sharedPref
        .getInt(UserPreferenceConstants.SLEEP_WINDOW_END_MIN_KEY,
            UserPreferenceConstants.DEFAULT_SLEEP_WINDOW_END.getMinutes());

    try {
      return new TimeOfTheDay(hour, minutes);
    } catch (IllegalArgumentException e) {
      return UserPreferenceConstants.DEFAULT_SLEEP_WINDOW_END;
    }
  }

  public void setFrequencyOfScan(final SharedPreferences sharedPref, int frequencyOfScan) {
    if (!isFrequencyOfScanValid(frequencyOfScan)) {
      throw new IllegalArgumentException(
          "Frequency of is out of bounds, trying to set: " + frequencyOfScan);
    }

    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(UserPreferenceConstants.FREQUENCY_OF_SCAN_KEY, frequencyOfScan);
    editor.commit();
  }

  private boolean isFrequencyOfScanValid(int frequencyOfScan) {
    return frequencyOfScan >= UserPreferenceConstants.MIN_FREQUENCY
        || frequencyOfScan <= UserPreferenceConstants.MAX_FREQUENCY;
  }

  public int getFrequencyOfScan(final SharedPreferences sharedPref) {
    int frequencyOfScan = sharedPref.getInt(UserPreferenceConstants.FREQUENCY_OF_SCAN_KEY,
        UserPreferenceConstants.DEFAULT_FREQUENCY);

    if (!isFrequencyOfScanValid(frequencyOfScan)) {
      return UserPreferenceConstants.DEFAULT_FREQUENCY;
    }

    return frequencyOfScan;
  }

  public void setNextScanTime(final SharedPreferences sharedPref, final Date date) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(UserPreferenceConstants.NEXT_SCAN_TIME_SET_KEY,
        UserPreferenceConstants.DATE_FORMAT.format(date));
    editor.commit();
  }

  public Date getNextScanTime(final SharedPreferences sharedPref) {
    String dateString = sharedPref.getString(UserPreferenceConstants.NEXT_SCAN_TIME_SET_KEY, null);
    if (dateString == null) {
      return null;
    }

    try {
      return UserPreferenceConstants.DATE_FORMAT.parse(dateString);
    } catch (ParseException e) {
      Log.i("UserPreferences", "Failed to load scan time, returning null");
      return null;
    }
  }

  private void validateTimeOfTheDay(int hour, int minutes) {
    //Validates Time of the day can be created with these values
    new TimeOfTheDay(hour, minutes);
  }
}
