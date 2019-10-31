package com.fourthlap.settingsscanner.scheduler;

import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import java.util.Calendar;

// Need custom scheduler as alarmManager.setRepeating
// doesn't work well with new android versions due to
// power saving modes
public class ScanTimeCalculator {

  /***
   * Calculates next scan time based on current time and frequency.
   * scan time are part of sequence frequency, 2*frequency, 3*frequency
   * For example with frequency 3 and currentTime between 3:00-5:59  will always return 6 as answer
   * @param currentTime
   * @param frequency
   * @return
   */
  public static Calendar getNextScanTime(final Calendar currentTime,final int frequency) {
    final Calendar scheduledDate = (Calendar) currentTime.clone();

    int currentHour = scheduledDate.get(Calendar.HOUR_OF_DAY);
    int reRunHour = (frequency * (currentHour / frequency)) + frequency;

    int difference = reRunHour - currentHour;

    if (reRunHour >= 24) {
      difference = (reRunHour % 24) + (24 - currentHour);
    }

    scheduledDate.add(Calendar.HOUR_OF_DAY, difference);
    scheduledDate.set(Calendar.MINUTE, 0);
    scheduledDate.set(Calendar.SECOND, 0);

    return scheduledDate;
  }

  /***
   * Calculates is current time falls in sleep window
   * @param currentTime
   * @param sleepWindowStartTime
   * @param sleepWindowEndTime
   * @return
   */
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
