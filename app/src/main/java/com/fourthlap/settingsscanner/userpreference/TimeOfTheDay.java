package com.fourthlap.settingsscanner.userpreference;

public class TimeOfTheDay {

  private final int hour;
  private final int minutes;

  public TimeOfTheDay(int hour, int minutes) {
    if (hour < 0 || hour > 24) {
      throw new IllegalArgumentException("Hour needs to be between 0 and 24, current value is: " + hour);
    }

    if (minutes < 0 || minutes > 60) {
      throw new IllegalArgumentException(
          "Minutes needs to be between 0 and 60, current value is: " + minutes);
    }

    this.hour = hour;
    this.minutes = minutes;
  }

  public int getHour() {
    return hour;
  }

  public int getMinutes() {
    return minutes;
  }

  public String getDisplayableString() {
    return String.format("%02d:%02d", hour, minutes);
  }

}
