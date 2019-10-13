package com.fourthlap.settingsscanner;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.fourthlap.settingsscanner.alarm.AlarmScheduler;
import com.fourthlap.settingsscanner.alarm.ScanTimeManager;
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.setting.Setting;
import com.fourthlap.settingsscanner.setting.SettingHandler;
import com.fourthlap.settingsscanner.setting.SettingsConfig;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
import java.util.Set;

public class PeriodicTaskExecutorService extends IntentService {

  private final SettingsConfig settingsConfiguration;
  private final UserPreferencesStore configurationStore;
  private final ReminderNotificationHandler reminderNotificationHandler;
  private final AlarmScheduler alarmScheduler;

  public PeriodicTaskExecutorService() {
    super("PeriodicTaskExecutorService");
    this.settingsConfiguration = new SettingsConfig();
    this.configurationStore = new UserPreferencesStore();
    this.reminderNotificationHandler = new ReminderNotificationHandler();
    this.alarmScheduler = new AlarmScheduler();
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    if (ScanTimeManager.isNightTime()) {
      Log.i("PeriodicTaskExecutor", "Received a scan request during sleep time, " +
          "ignoring and setting next scan");
      alarmScheduler.scheduleAlarm(getApplicationContext());
      return;
    }

    final Context context = getApplicationContext();
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);
    final NotificationManager notificationManager = (NotificationManager) getSystemService(
        Context.NOTIFICATION_SERVICE);

    final Set<Setting> whitelistedSettings = configurationStore
        .getSettingsEnabledForWatch(sharedPreferences);
    for (final Setting setting : whitelistedSettings) {
      SettingHandler handler = settingsConfiguration.getHandler(setting);
      if (handler.isEnabled(context)) {
        Log.i("PeriodicTaskExecutor",
            String.format("%s setting is enabled, notifying user", setting));
        reminderNotificationHandler.notifyUser(context, notificationManager);
        return;
      }
    }

    Log.i("PeriodicTaskExecutor", "Required settings are now disabled, cancelling notification");
    reminderNotificationHandler.cancelNotification(notificationManager);
  }
}
