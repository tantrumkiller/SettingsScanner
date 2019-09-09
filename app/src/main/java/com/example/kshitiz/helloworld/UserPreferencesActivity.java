package com.example.kshitiz.helloworld;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.kshitiz.helloworld.setting.Setting;
import com.example.kshitiz.helloworld.setting.SettingsConfig;
import com.example.kshitiz.helloworld.userpreference.UserPreferencesStore;
import com.example.kshitiz.helloworld.notification.ReminderNotificationHandler;

import java.util.Set;

public class UserPreferencesActivity extends AppCompatActivity {
    private static final long TWO_HRS = AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15;

    private final UserPreferencesStore configurationStore;
    private final ReminderNotificationHandler reminderNotificationHandler;
    private final SettingsConfig settingConfiguration;

    public UserPreferencesActivity(){
        super();
        this.configurationStore = new UserPreferencesStore();
        this.reminderNotificationHandler = new ReminderNotificationHandler();
        this.settingConfiguration = new SettingsConfig();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_display);

        populateUserPrefs();
        scheduleAlarm();
        setupNotificationChannel();
    }

    private void populateUserPrefs() {
        final Context context = getApplicationContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        final Set<Setting> userConfiguration = configurationStore.getSettingsEnabledForWatch(sharedPreferences);

        for(final Setting setting: Setting.values()){
            final Switch switchButton = findViewById(settingConfiguration.getPreferenceButtonId(setting));
            if(userConfiguration.contains(setting)){
                switchButton.setChecked(true);
            }
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    configurationStore.updateSettingPreference(sharedPreferences, setting, isChecked);
                }
            });
        }
    }

    public void scheduleAlarm() {
        final Intent intent = new Intent(getApplicationContext(), TimerBroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, TimerBroadcastReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // alarm is set right away
        long firstMillis = System.currentTimeMillis();

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, TWO_HRS, pIntent);
    }

    private void setupNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            reminderNotificationHandler.createNotificationChannel(getSystemService(NotificationManager.class));
        }
    }
}
