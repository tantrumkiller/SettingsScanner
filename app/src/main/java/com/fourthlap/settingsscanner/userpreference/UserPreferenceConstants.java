package com.fourthlap.settingsscanner.userpreference;

import java.text.SimpleDateFormat;

public final class UserPreferenceConstants {

  private UserPreferenceConstants() {

  }

  public static final int MAX_FREQUENCY = 6;
  public static final int MIN_FREQUENCY = 1;

  static final int DEFAULT_FREQUENCY = 3;

  static final String SLEEP_WINDOW_START_HOUR_KEY = "SleepWindowStartHour";
  static final String SLEEP_WINDOW_START_MIN_KEY = "SleepWindowStartMinutes";
  static final String SLEEP_WINDOW_END_HOUR_KEY = "SleepWindowEndHour";
  static final String SLEEP_WINDOW_END_MIN_KEY = "SleepWindowEndMinutes";
  static final String FREQUENCY_OF_SCAN_KEY = "frequencyOfScan";
  static final String NEXT_SCAN_TIME_SET_KEY = "nextScanTime";

  static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
      "E MMM dd HH:mm:ss Z yyy");

  static final TimeOfTheDay DEFAULT_SLEEP_WINDOW_START = new TimeOfTheDay(22, 0);
  static final TimeOfTheDay DEFAULT_SLEEP_WINDOW_END = new TimeOfTheDay(8, 0);
}
