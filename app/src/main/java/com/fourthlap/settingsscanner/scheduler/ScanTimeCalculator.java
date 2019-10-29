package com.fourthlap.settingsscanner.scheduler;

import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import java.util.Calendar;

// Need custom scheduler as alarmManager.setRepeating
// doesn't work well with new android versions due to
// power saving modes
public class ScanTimeCalculator {

  public static Calendar getNextScanTime(final Calendar currentTime, final int frequency) {
    final Calendar scheduledDate = (Calendar) currentTime.clone();
    scheduledDate.add(Calendar.HOUR_OF_DAY, frequency);
    scheduledDate.set(Calendar.MINUTE, 0);
    scheduledDate.set(Calendar.SECOND, 0);

    return scheduledDate;
  }

  public static boolean isSleepTime(final Calendar currentTime,
      final TimeOfTheDay sleepWindowStartTime,
      final TimeOfTheDay sleepWindowEndTime) {

    final Calendar lastSleepWindowStartTime = getLastSleepTimeStartTime(currentTime,
        sleepWindowStartTime);

    final Calendar latestSleepWindowEndTime = getLatestSleepWindowEndTime(lastSleepWindowStartTime,
        sleepWindowEndTime,
        isSameDay(sleepWindowStartTime, sleepWindowEndTime));

    if (currentTime.after(lastSleepWindowStartTime) && currentTime
        .before(latestSleepWindowEndTime)) {
      return true;
    }

    return false;
  }

  private static Calendar getLastSleepTimeStartTime(final Calendar rightNow,
      final TimeOfTheDay sleepTimeStart) {
    final Calendar lastSleepTimeStartDate = Calendar.getInstance();
    lastSleepTimeStartDate.set(rightNow.get(Calendar.YEAR),
        rightNow.get(Calendar.MONTH),
        rightNow.get(Calendar.DATE),
        sleepTimeStart.getHour(),
        sleepTimeStart.getMinutes(),
        0);

    if (lastSleepTimeStartDate.after(rightNow)) {
      lastSleepTimeStartDate.add(Calendar.DATE, -1);
    }
    return lastSleepTimeStartDate;
  }

  private static Calendar getLatestSleepWindowEndTime(Calendar lastSleepTimeStartDate,
      TimeOfTheDay sleepTimeEnd,
      boolean doesSleepWindowEndsNextDay) {

    final Calendar latestSleepTimeEndDate = Calendar.getInstance();
    latestSleepTimeEndDate.set(lastSleepTimeStartDate.get(Calendar.YEAR),
        lastSleepTimeStartDate.get(Calendar.MONTH),
        lastSleepTimeStartDate.get(Calendar.DATE),
        sleepTimeEnd.getHour(),
        sleepTimeEnd.getMinutes(),
        0);

    if (!doesSleepWindowEndsNextDay) {
      latestSleepTimeEndDate.add(Calendar.DATE, 1);
    }

    return latestSleepTimeEndDate;
  }

  private static boolean isSameDay(final TimeOfTheDay earlierTime,
      final TimeOfTheDay laterTime) {
    if (earlierTime.getHour() <= laterTime.getHour()) {
      return true;
    }

    return false;
  }
}
