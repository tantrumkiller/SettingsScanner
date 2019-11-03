package com.fourthlap.settingsscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.fourthlap.settingsscanner.setting.Setting;
import com.fourthlap.settingsscanner.setting.SettingsConfiguration;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;
import com.fourthlap.settingsscanner.viewelements.FrequencyButton;
import com.fourthlap.settingsscanner.viewelements.SleepWindowEndButton;
import com.fourthlap.settingsscanner.viewelements.SleepWindowStartButton;
import java.util.Set;

public class UserPreferencesActivity extends AppCompatActivity {
  private final UserPreferences userPreferences;
  private final SettingsConfiguration settingConfiguration;

  public UserPreferencesActivity() {
    super();
    this.userPreferences = new UserPreferences();
    this.settingConfiguration = new SettingsConfiguration();
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_preferences_page);

    setupToolbar();
    populateUserPreferences();
    setupTimerPreferencesButtons();
  }

    private void setupToolbar() {
    final Toolbar toolbar = findViewById(R.id.toolbar_user_preferences);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), ActionsRequiredActivity.class));
      }
    });
  }

  private void populateUserPreferences() {
    final SharedPreferences sharedPreferences = getSharedPreferences();

    final Set<Setting> settingsToBeScanned = userPreferences
        .getSettingsToBeScanned(sharedPreferences);

    for (final Setting setting : Setting.values()) {
      final Switch switchButton = findViewById(settingConfiguration.getPreferenceButtonId(setting));

      if (settingsToBeScanned.contains(setting)) {
        switchButton.setChecked(true);
      } else {
        switchButton.setChecked(false);
      }

      switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
          userPreferences
              .updateSettingsToBeScannedPreferences(sharedPreferences, setting, isChecked);
        }
      });
    }
  }

  private void setupTimerPreferencesButtons() {
    final SharedPreferences sharedPreferences = getSharedPreferences();

    final Button sleepTimeStartButton = findViewById(R.id.sleepHourStartButton);
    sleepTimeStartButton
        .setOnClickListener(new SleepWindowStartButton(userPreferences, sleepTimeStartButton));
    sleepTimeStartButton.setText(
        userPreferences.getSleepWindowStartTime(sharedPreferences).getDisplayableString());

    final Button sleepEndStartButton = findViewById(R.id.sleepHourEndButton);
    sleepEndStartButton
        .setOnClickListener(new SleepWindowEndButton(userPreferences, sleepEndStartButton));
    sleepEndStartButton.setText(
        userPreferences.getSleepWindowEndTime(sharedPreferences).getDisplayableString());

    final Button timerFrequencyButton = findViewById(R.id.frequencySelectorButton);
    new FrequencyButton(timerFrequencyButton, userPreferences).setupButton(getApplicationContext());
  }

  private SharedPreferences getSharedPreferences() {
    return PreferenceManager
        .getDefaultSharedPreferences(getApplicationContext());
  }
}
