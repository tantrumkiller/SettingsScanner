package com.example.kshitiz.helloworld;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.kshitiz.helloworld.setting.Setting;
import com.example.kshitiz.helloworld.setting.SettingsConfig;
import com.example.kshitiz.helloworld.setting.SettingHandler;
import com.example.kshitiz.helloworld.userpreference.UserPreferencesStore;
import com.example.kshitiz.helloworld.notification.ReminderNotificationHandler;

import java.util.Set;

public class PeriodicTaskExecutorService extends IntentService {
    private final SettingsConfig settingsConfiguration;
    private final UserPreferencesStore configurationStore;
    private final ReminderNotificationHandler reminderNotificationHandler;

    public PeriodicTaskExecutorService() {
        super("PeriodicTaskExecutorService");
        this.settingsConfiguration = new SettingsConfig();
        this.configurationStore = new UserPreferencesStore();
        this.reminderNotificationHandler = new ReminderNotificationHandler();
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final Context context = getApplicationContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final Set<Setting> whitelistedSettings = configurationStore.getSettingsEnabledForWatch(sharedPreferences);
        for (final Setting setting : whitelistedSettings) {
            SettingHandler handler = settingsConfiguration.getHandler(setting);
            if (handler.isEnabled(context)) {
                Log.i("PeriodicTaskExecutorService", String.format("%s setting is enabled, notifying user", setting));
                reminderNotificationHandler.notifyUser(context, notificationManager);
                return;
            }
        }

        Log.i("PeriodicTaskExecutorService", "Required settings are now disabled, canceling older notification");
        reminderNotificationHandler.cancelNotification(notificationManager);
    }
}
