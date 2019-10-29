package com.fourthlap.settingsscanner;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.scheduler.ScanScheduler;
import com.fourthlap.settingsscanner.scheduler.ScanTimeCalculator;
import com.fourthlap.settingsscanner.setting.SettingsConfig;
import com.fourthlap.settingsscanner.setting.SettingsScanner;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
import java.util.Calendar;

public class PeriodicTaskExecutorService extends IntentService {

  private final ReminderNotificationHandler reminderNotificationHandler;
  private final SettingsScanner settingsScanner;
  private final ScanScheduler scanScheduler;
  private final UserPreferencesStore userPreferencesStore;

  public PeriodicTaskExecutorService() {
    super("PeriodicTaskExecutorService");
    this.reminderNotificationHandler = new ReminderNotificationHandler();
    this.userPreferencesStore = new UserPreferencesStore();
    this.scanScheduler = new ScanScheduler(userPreferencesStore);
    this.settingsScanner = new SettingsScanner(userPreferencesStore, new SettingsConfig());
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    final Context context = getApplicationContext();

    //Schedule next scan
    scanScheduler.scheduleNextScan(context);

    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(getApplicationContext());

    final TimeOfTheDay sleepWindowStart = userPreferencesStore
        .getSleepWindowStartTime(sharedPreferences);
    final TimeOfTheDay sleepWindowEnd = userPreferencesStore
        .getSleepWindowEndTime(sharedPreferences);

    if (ScanTimeCalculator.isSleepTime(Calendar.getInstance(), sleepWindowStart, sleepWindowEnd)) {
      Log.i("PeriodicTaskExecutor", "Received a scan request during sleep time, " +
          "ignoring and setting next scan");
      return;
    }

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
