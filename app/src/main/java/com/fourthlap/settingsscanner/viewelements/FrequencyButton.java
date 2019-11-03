package com.fourthlap.settingsscanner.viewelements;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import com.fourthlap.settingsscanner.R;
import com.fourthlap.settingsscanner.userpreference.UserPreferenceConstants;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;

public class FrequencyButton {

  private String hours_suffix = "hours";

  private final Button frequencyOfScanButton;
  private final UserPreferences userPreferences;

  public FrequencyButton(final Button frequencyOfScanButton,
      final UserPreferences userPreferences) {
    this.frequencyOfScanButton = frequencyOfScanButton;
    this.userPreferences = userPreferences;
  }

  public void setupButton(final Context context) {
    hours_suffix = context.getResources().getString(R.string.hours_suffix);
    final String frequency = String.valueOf(userPreferences
        .getFrequencyOfScan(PreferenceManager.getDefaultSharedPreferences(context)));

    frequencyOfScanButton.setText(getFormattedFrequencyOfScan(frequency));

    frequencyOfScanButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        show(v.getContext());
      }
    });
  }

  private void show(final Context context) {

    final Dialog numberPickerDialog = new Dialog(context);
    numberPickerDialog
        .setTitle(context.getResources().getString(R.string.frequency_of_scan_dialog_heading));
    numberPickerDialog.setContentView(R.layout.number_picker_widget);

    final Button setButton = numberPickerDialog.findViewById(R.id.frequency_selector_set_button);
    final Button cancelButton = numberPickerDialog.findViewById(R.id.frequency_selector_cancel_button);

    final NumberPicker numberPicker = numberPickerDialog.findViewById(R.id.frequency_selector);
    numberPicker.setMaxValue(UserPreferenceConstants.MAX_FREQUENCY);
    numberPicker.setMinValue(UserPreferenceConstants.MIN_FREQUENCY);
    numberPicker.setValue(userPreferences
        .getFrequencyOfScan(PreferenceManager.getDefaultSharedPreferences(context)));
    numberPicker.setWrapSelectorWheel(false);

    setButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final SharedPreferences sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(v.getContext());
        userPreferences.setFrequencyOfScan(sharedPreferences, numberPicker.getValue());

        frequencyOfScanButton
            .setText(getFormattedFrequencyOfScan(String.valueOf(numberPicker.getValue())));
        numberPickerDialog.dismiss();
      }
    });

    cancelButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        numberPickerDialog.dismiss();
      }
    });

    numberPickerDialog.show();
  }

  @NonNull
  private String getFormattedFrequencyOfScan(final String frequency) {
    return frequency + " " + hours_suffix;
  }
}
