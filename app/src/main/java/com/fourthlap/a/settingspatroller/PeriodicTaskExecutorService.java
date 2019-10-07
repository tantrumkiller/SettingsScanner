package com.fourthlap.a.settingspatroller;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fourthlap.a.settingspatroller.notification.ReminderNotificationHandler;
import com.fourthlap.a.settingspatroller.setting.Setting;
import com.fourthlap.a.settingspatroller.setting.SettingHandler;
import com.fourthlap.a.settingspatroller.setting.SettingsConfig;
import com.fourthlap.a.settingspatroller.userpreference.UserPreferencesStore;

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
                Log.i("PeriodicTaskExecutor", String.format("%s setting is enabled, notifying user", setting));
                reminderNotificationHandler.notifyUser(context, notificationManager);
                return;
            }
        }

        Log.i("PeriodicTaskExecutor", "Required settings are now disabled, canceling older notification");
        reminderNotificationHandler.cancelNotification(notificationManager);
    }
}
