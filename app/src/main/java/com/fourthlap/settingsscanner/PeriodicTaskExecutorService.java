package com.fourthlap.settingsscanner;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.fourthlap.settingsscanner.alarm.AlarmScheduler;
import com.fourthlap.settingsscanner.alarm.ScanTimeManager;
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.setting.SettingsConfig;
import com.fourthlap.settingsscanner.setting.SettingsScanner;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;

public class PeriodicTaskExecutorService extends IntentService {
  private final ReminderNotificationHandler reminderNotificationHandler;
  private final SettingsScanner settingsScanner;
  private final AlarmScheduler alarmScheduler;

  public PeriodicTaskExecutorService() {
    super("PeriodicTaskExecutorService");
    this.reminderNotificationHandler = new ReminderNotificationHandler();
    this.alarmScheduler = new AlarmScheduler();
    this.settingsScanner = new SettingsScanner(new UserPreferencesStore(),  new SettingsConfig());
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
    final NotificationManager notificationManager = (NotificationManager) getSystemService(
        Context.NOTIFICATION_SERVICE);

    if (settingsScanner.isAnySettingEnabled(context)) {
      Log.i("PeriodicTaskExecutor", "Some settings are enabled, notifying user");
      reminderNotificationHandler.notifyUser(context, notificationManager);
    } else {
      Log.i("PeriodicTaskExecutor", "Required settings are now disabled, cancelling notification");
      reminderNotificationHandler.cancelNotification(notificationManager);
    }
  }
}
