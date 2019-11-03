package com.fourthlap.settingsscanner;

import java.util.Calendar;

public class TestData {

  public static final int DEFAULT_YEAR = 1990;
  public static final int DECEMBER = 11;
  public static final int ELEVENTH = 11;

  public static Calendar getCalendar(int i) {
    final Calendar time = Calendar.getInstance();
    time.set(Calendar.YEAR, DEFAULT_YEAR);
    time.set(Calendar.MONTH, DECEMBER);
    time.set(Calendar.DAY_OF_MONTH, ELEVENTH);
    time.set(Calendar.HOUR_OF_DAY, i);
    time.set(Calendar.MINUTE, 0);
    time.set(Calendar.SECOND, 0);
    return time;
  }
}
