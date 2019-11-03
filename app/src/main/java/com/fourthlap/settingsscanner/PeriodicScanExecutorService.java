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
import com.fourthlap.settingsscanner.setting.SettingsConfiguration;
import com.fourthlap.settingsscanner.setting.SettingsScanner;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;
import java.util.Calendar;

public class PeriodicScanExecutorService extends IntentService {

  private final ReminderNotificationHandler reminderNotificationHandler;
  private final SettingsScanner settingsScanner;
  private final ScanScheduler scanScheduler;
  private final UserPreferences userPreferences;

  public PeriodicScanExecutorService() {
    super("PeriodicScanExecutorService");
    this.reminderNotificationHandler = new ReminderNotificationHandler();
    this.userPreferences = new UserPreferences();
    this.scanScheduler = new ScanScheduler(userPreferences);
    this.settingsScanner = new SettingsScanner(userPreferences, new SettingsConfiguration());
  }

  //Visible for testing
  PeriodicScanExecutorService(final ReminderNotificationHandler reminderNotificationHandler,
      final SettingsScanner settingsScanner, final ScanScheduler scanScheduler,
      final UserPreferences userPreferences) {
    super("PeriodicScanExecutorService");
    this.reminderNotificationHandler = reminderNotificationHandler;
    this.userPreferences = userPreferences;
    this.scanScheduler = scanScheduler;
    this.settingsScanner = settingsScanner;
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    final Context context = getApplicationContext();
    final Calendar currentTime = Calendar.getInstance();

    executePeriodicScan(context, currentTime);
  }

  //Visible for testing
  void executePeriodicScan(final Context context, final Calendar currentTime) {
    scanScheduler.scheduleNextScan(context, currentTime);

    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(getApplicationContext());

    final TimeOfTheDay sleepWindowStart = userPreferences
        .getSleepWindowStartTime(sharedPreferences);
    final TimeOfTheDay sleepWindowEnd = userPreferences
        .getSleepWindowEndTime(sharedPreferences);

    if (ScanTimeCalculator.isSleepTime(currentTime, sleepWindowStart, sleepWindowEnd)) {
      Log.i("PeriodicTaskExecutor", "Received a scan request during sleep time, " +
          "ignoring and setting next scan");
      return;
    }

    final NotificationManager notificationManager = (NotificationManager) context.getSystemService(
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
