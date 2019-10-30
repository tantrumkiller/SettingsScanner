package com.fourthlap.settingsscanner.viewelements;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import com.fourthlap.settingsscanner.R;
import com.fourthlap.settingsscanner.UserPreferencesActivity;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;

public class FrequencyButton implements OnClickListener, NumberPicker.OnValueChangeListener {

  private final Button b;
  private final UserPreferencesStore userPreferencesStore;

  public FrequencyButton(Button b, UserPreferencesStore userPreferencesStore) {
    this.b = b;
    this.userPreferencesStore = userPreferencesStore;
  }

  public void setupButton(Context context) {
    b.setText(String.valueOf(userPreferencesStore
        .getFrequencyOfScan(PreferenceManager.getDefaultSharedPreferences(context))));
    b.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        show(v.getContext());
      }
    });
  }

  @Override
  public void onClick(View v) {
    final Context context = v.getContext();
    context.startActivity(new Intent(context, UserPreferencesActivity.class));
  }

  @Override
  public void onValueChange(NumberPicker numberPicker, int i, int i1) {
    Log.d("FrequencyButton", "onValueChange: ");
  }

  private void show(Context context) {

    final Dialog d = new Dialog(context);
    d.setTitle("NumberPicker");
    d.setContentView(R.layout.number_picker);
    Button b1 = d.findViewById(R.id.button1);
    Button b2 = d.findViewById(R.id.button2);

    final NumberPicker np = d.findViewById(R.id.numberPicker1);
    np.setMaxValue(6);
    np.setMinValue(1);
    np.setValue(userPreferencesStore
        .getFrequencyOfScan(PreferenceManager.getDefaultSharedPreferences(context)));
    np.setWrapSelectorWheel(false);
    np.setOnValueChangedListener(this);

    b1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        b.setText(String.valueOf(np.getValue()));

        final SharedPreferences sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(v.getContext());

        userPreferencesStore.setFrequencyOfScan(sharedPreferences, np.getValue());
        d.dismiss();
      }
    });

    b2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        d.dismiss();
      }
    });

    d.show();
  }
}
