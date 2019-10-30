package com.fourthlap.settingsscanner.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import com.fourthlap.settingsscanner.TimerBroadcastReceiver;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
import java.util.Calendar;
import java.util.Date;

public class ScanScheduler {

  private final UserPreferencesStore userPreferencesStore;

  public ScanScheduler(final UserPreferencesStore userPreferencesStore) {
    this.userPreferencesStore = userPreferencesStore;
  }

  public void scheduleNextScan(final Context context) {
    final Intent intent = new Intent(context, TimerBroadcastReceiver.class);

    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);

    final Date existingTime = userPreferencesStore.getNextScanTime(sharedPreferences);
    Log.i("ScanScheduler", "Existing scan time in preference store: " + existingTime);

    final PendingIntent updatedPendingIntent = PendingIntent
        .getBroadcast(context, TimerBroadcastReceiver.REQUEST_CODE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT);

    final Calendar nextScanTime = ScanTimeCalculator.getNextScanTime(Calendar.getInstance(),
        userPreferencesStore.getFrequencyOfScan(sharedPreferences));

    final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextScanTime.getTimeInMillis(),
          updatedPendingIntent);
    } else {
      alarm.setExact(AlarmManager.RTC_WAKEUP, nextScanTime.getTimeInMillis(), updatedPendingIntent);
    }

    Log.i("ScanScheduler", "Set next scan time as: " + nextScanTime.getTime());
    userPreferencesStore.setNextScanTime(sharedPreferences, nextScanTime.getTime());
  }
}