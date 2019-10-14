package com.fourthlap.settingsscanner.alarm;

import java.util.Calendar;

// Need custom scheduler as alarmManager.setRepeating
// doesn't work well with new android versions due to
// power saving modes
public class ScanTimeManager {

  private static final int SLEEP_TIME_START_HOUR = 22;
  private static final int SLEEP_TIME_END_HOUR = 8;
  //Schedules re-runs after every x hours
  private static final int RERUN_AFTER_HOURS = 3;

  public static Calendar getNextScanTime() {
    final Calendar rightNow = Calendar.getInstance();
    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

    Calendar scheduledDate;

    //Before Mid-Night hours, set next day post sleep time hour
    if (currentHour >= SLEEP_TIME_START_HOUR) {
      final Calendar currentTime = Calendar.getInstance();
      currentTime.add(Calendar.DATE, 1);

      scheduledDate = Calendar.getInstance();
      scheduledDate.set(currentTime.get(Calendar.YEAR),
          currentTime.get(Calendar.MONTH),
          currentTime.get(Calendar.DATE),
          SLEEP_TIME_END_HOUR + 1,
          0,
          0);

    } else if (currentHour >= 0 && currentHour <= SLEEP_TIME_END_HOUR) {
      //Post Mid-night night hours, set same day post sleep time hour
      scheduledDate = Calendar.getInstance();
      scheduledDate.set(rightNow.get(Calendar.YEAR),
          rightNow.get(Calendar.MONTH),
          rightNow.get(Calendar.DATE),
          SLEEP_TIME_END_HOUR + 1,
          0,
          0);
    } else {
      //Re-run after every "RERUN_AFTER_HOURS" number of hours
      int reRunHour = RERUN_AFTER_HOURS * (rightNow.get(Calendar.HOUR_OF_DAY) / RERUN_AFTER_HOURS)
          + RERUN_AFTER_HOURS;

      scheduledDate = Calendar.getInstance();
      scheduledDate.set(rightNow.get(Calendar.YEAR),
          rightNow.get(Calendar.MONTH),
          rightNow.get(Calendar.DATE),
          reRunHour,
          0,
          0);
    }

    return scheduledDate;
  }

  public static boolean isNightTime() {
    final Calendar rightNow = Calendar.getInstance();
    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

    if (currentHour > SLEEP_TIME_START_HOUR || (currentHour >= 0
        && currentHour <= SLEEP_TIME_END_HOUR)) {
      return true;
    }

    return false;
  }
}
