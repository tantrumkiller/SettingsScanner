package com.fourthlap.settingsscanner.viewelements;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;

public class SleepWindowStartButton implements OnClickListener, OnTimeSetListener {

  private final UserPreferences userPreferences;
  private final Button button;

  public SleepWindowStartButton(final UserPreferences userPreferences, final Button button) {
    this.userPreferences = userPreferences;
    this.button = button;
  }

  public void onClick(final View v) {
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(v.getContext());
    final TimeOfTheDay timeOfTheDay = userPreferences.getSleepWindowStartTime(sharedPreferences);

    final TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), this,
        timeOfTheDay.getHour(),
        timeOfTheDay.getMinutes(), true);

    timePickerDialog.show();
  }

  public void onTimeSet(final TimePicker view, int hourOfDay, int minute) {
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(view.getContext());

    userPreferences.setSleepWindowStartTime(sharedPreferences, hourOfDay, minute);

    button.setText(new TimeOfTheDay(hourOfDay, minute).getDisplayableString());
  }
}