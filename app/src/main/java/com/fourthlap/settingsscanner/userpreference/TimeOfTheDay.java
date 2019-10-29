package com.fourthlap.settingsscanner.userpreference;

public class TimeOfTheDay {

  private final int hour;
  private final int minutes;

  public TimeOfTheDay(int hour, int minutes) {
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
