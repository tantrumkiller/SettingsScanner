package com.example.kshitiz.helloworld;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.kshitiz.helloworld.alarm.AlarmScheduler;
import com.example.kshitiz.helloworld.notification.ReminderNotificationHandler;
import com.example.kshitiz.helloworld.setting.Setting;
import com.example.kshitiz.helloworld.setting.SettingsConfig;
import com.example.kshitiz.helloworld.userpreference.UserPreferencesStore;

import java.util.Set;

public class UserPreferencesActivity extends AppCompatActivity {
    private final UserPreferencesStore configurationStore;
    private final ReminderNotificationHandler reminderNotificationHandler;
    private final SettingsConfig settingConfiguration;
    private final AlarmScheduler alarmScheduler;

    public UserPreferencesActivity() {
        super();
        this.configurationStore = new UserPreferencesStore();
        this.reminderNotificationHandler = new ReminderNotificationHandler();
        this.settingConfiguration = new SettingsConfig();
        this.alarmScheduler = new AlarmScheduler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        setupToolbar();
        populateUserPrefrences();
        alarmScheduler.scheduleAlarm(getApplicationContext());
        setupNotificationChannel();
    }

    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_user_preferences);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActionsRequiredActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateUserPrefrences() {
        final Context context = getApplicationContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        final Set<Setting> userConfiguration = configurationStore.getSettingsEnabledForWatch(sharedPreferences);

        for (final Setting setting : Setting.values()) {
            final Switch switchButton = findViewById(settingConfiguration.getPreferenceButtonId(setting));
            if (userConfiguration.contains(setting)) {
                switchButton.setChecked(true);
            }
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    configurationStore.updateSettingPreference(sharedPreferences, setting, isChecked);
                }
            });
        }
    }

    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            reminderNotificationHandler.createNotificationChannel(getSystemService(NotificationManager.class));
        }
    }
}
