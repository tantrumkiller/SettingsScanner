package com.fourthlap.settingsscanner.viewelements;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;

public class SleepWindowStartButton implements OnClickListener, OnTimeSetListener {

  private final UserPreferencesStore userPreferencesStore;
private final Button button;

  public SleepWindowStartButton(final UserPreferencesStore userPreferencesStore, Button button){
    this.userPreferencesStore = userPreferencesStore;
    this.button = button;
  }

  public void onClick(View v) {
    Context context = v.getContext();
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);
    final TimeOfTheDay timeOfTheDay = userPreferencesStore.getSleepWindowStartTime(sharedPreferences);
    TimePickerDialog tp1 = new TimePickerDialog(v.getContext(), this, timeOfTheDay.getHour(),
        timeOfTheDay.getMinutes(), true);
    tp1.show();
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    Context context = view.getContext();
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);

    userPreferencesStore.setSleepWindowStartTime(sharedPreferences, hourOfDay, minute);
    button.setText(new TimeOfTheDay(hourOfDay, minute).getDisplayableString());
  }
}