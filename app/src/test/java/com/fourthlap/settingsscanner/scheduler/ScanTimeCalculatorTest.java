package com.fourthlap.settingsscanner.scheduler;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.support.annotation.NonNull;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import java.util.Calendar;
import org.junit.Test;

public class ScanTimeCalculatorTest {
  static final int DEFAULT_YEAR = 1990;
  static final int DECEMBER = 11;
  static final int ELEVENTH = 11;

  private static final TimeOfTheDay EIGHT_AM = new TimeOfTheDay(8, 0);
  private static final TimeOfTheDay TWO_PM = new TimeOfTheDay(14, 0);
  private static final TimeOfTheDay TEN_PM = new TimeOfTheDay(22, 0);


  @Test
  public void testGetNextScanTime_sameDay() {
    final Calendar time = getCalendar(2);
    final Calendar scanTime = ScanTimeCalculator.getNextScanTime(time, 5);

    assertThat(scanTime.get(Calendar.YEAR), is(DEFAULT_YEAR));
    assertThat(scanTime.get(Calendar.MONTH), is(DECEMBER));
    assertThat(scanTime.get(Calendar.DAY_OF_MONTH), is(ELEVENTH));
    assertThat(scanTime.get(Calendar.HOUR_OF_DAY), is(5));
    assertThat(scanTime.get(Calendar.MINUTE), is(0));
    assertThat(scanTime.get(Calendar.SECOND), is(0));
  }

  @Test
  public void testGetNextScanTime_nextDay() {
    Calendar time = getCalendar(23);

    Calendar scanTime = ScanTimeCalculator.getNextScanTime(time, 5);

    assertThat(scanTime.get(Calendar.YEAR), is(DEFAULT_YEAR));
    assertThat(scanTime.get(Calendar.MONTH), is(DECEMBER));
    assertThat(scanTime.get(Calendar.DAY_OF_MONTH), is(ELEVENTH + 1));
    assertThat(scanTime.get(Calendar.HOUR), is(1));
    assertThat(scanTime.get(Calendar.MINUTE), is(0));
    assertThat(scanTime.get(Calendar.SECOND), is(0));
  }

  @Test
  public void testIsSleepTime_windowEndsSameDay_currentTimeBeforeWindow() {
    final Calendar calendar = getCalendar(6);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, EIGHT_AM, TWO_PM);

    assertFalse(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowEndsSameDay_currentTimeWithinWindow() {
    final Calendar calendar = getCalendar(9);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, EIGHT_AM, TWO_PM);

    assertTrue(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowEndsSameDay_currentTimeAfterWindow() {
    final Calendar calendar = getCalendar(15);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, EIGHT_AM, TWO_PM);

    assertFalse(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowEndsNextDay_currentTimeBeforeWindow() {
    final Calendar calendar = getCalendar(21);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, TEN_PM, TWO_PM);

    assertFalse(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowEndsNextDay_currentTimeDuringWindow() {
    final Calendar calendar = getCalendar(1);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, TEN_PM, TWO_PM);

    assertTrue(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowEndsNextDay_currentTimeAfterWindow() {
    final Calendar calendar = getCalendar(9);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, TEN_PM, EIGHT_AM);

    assertFalse(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowStartIsExcluded() {
    final Calendar calendar = getCalendar(8);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, EIGHT_AM, TWO_PM);

    assertFalse(isSleepTime);
  }

  @Test
  public void testIsSleepTime_windowEndIsIncluded() {
    final Calendar calendar = getCalendar(14);
    final boolean isSleepTime = ScanTimeCalculator
        .isSleepTime(calendar, EIGHT_AM, TWO_PM);

    assertTrue(isSleepTime);
  }

  @NonNull
  static Calendar getCalendar(int i) {
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