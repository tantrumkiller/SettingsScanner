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
import com.fourthlap.settingsscanner.setting.SettingsConfig;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
import com.fourthlap.settingsscanner.viewelements.SleepWindowEndButton;
import com.fourthlap.settingsscanner.viewelements.SleepWindowStartButton;
import java.util.Set;

public class UserPreferencesActivity extends AppCompatActivity {

  private final UserPreferencesStore configurationStore;
  private final SettingsConfig settingConfiguration;

  public UserPreferencesActivity() {
    super();
    this.configurationStore = new UserPreferencesStore();
    this.settingConfiguration = new SettingsConfig();
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.preferences);

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
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(getApplicationContext());

    final Set<Setting> userConfiguration = configurationStore
        .getSettingsToBeScanned(sharedPreferences);

    for (final Setting setting : Setting.values()) {
      final Switch switchButton = findViewById(settingConfiguration.getPreferenceButtonId(setting));

      if (userConfiguration.contains(setting)) {
        switchButton.setChecked(true);
      } else {
        switchButton.setChecked(false);
      }

      switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
          configurationStore.updateSettingsToBeScannedPreferences(sharedPreferences, setting, isChecked);
        }
      });
    }
  }

  private void setupTimerPreferencesButtons() {
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(getApplicationContext());

    final Button sleepTimeStartButton = findViewById(R.id.sleepHourStartButton);
    sleepTimeStartButton.setOnClickListener(new SleepWindowStartButton(configurationStore, sleepTimeStartButton));
    sleepTimeStartButton.setText(
        configurationStore.getSleepWindowStartTime(sharedPreferences).getDisplayableString());

    final Button sleepEndStartButton = findViewById(R.id.sleepHourEndButton);
    sleepEndStartButton.setOnClickListener(new SleepWindowEndButton(configurationStore, sleepEndStartButton));
    sleepEndStartButton.setText(
        configurationStore.getSleepWindowEndTime(sharedPreferences).getDisplayableString());
  }
}
