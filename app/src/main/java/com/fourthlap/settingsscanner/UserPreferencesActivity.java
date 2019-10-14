package com.fourthlap.settingsscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.fourthlap.settingsscanner.setting.Setting;
import com.fourthlap.settingsscanner.setting.SettingsConfig;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
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
        .getSettingsEnabledForWatch(sharedPreferences);

    for (final Setting setting : Setting.values()) {
      final Switch switchButton = findViewById(settingConfiguration.getPreferenceButtonId(setting));

      if (userConfiguration.contains(setting)) {
        switchButton.setChecked(true);
      } else {
        switchButton.setChecked(false);
      }

      switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
          configurationStore.updateSettingPreference(sharedPreferences, setting, isChecked);
        }
      });
    }
  }
}
