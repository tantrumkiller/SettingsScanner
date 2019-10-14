package com.fourthlap.settingsscanner.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.fourthlap.settingsscanner.TimerBroadcastReceiver;
import java.util.Calendar;

public class AlarmScheduler {
  public void scheduleAlarm(final Context context) {
    final Intent intent = new Intent(context, TimerBroadcastReceiver.class);

    final PendingIntent pIntent = PendingIntent
        .getBroadcast(context, TimerBroadcastReceiver.REQUEST_CODE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT);

    final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    final Calendar nextScanTime = ScanTimeManager.getNextScanTime();
    Log.i("AlarmScheduler", "Setting scan time for: " + nextScanTime.getTime());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextScanTime.getTimeInMillis(),
          pIntent);
    } else {
      alarm.setExact(AlarmManager.RTC_WAKEUP, nextScanTime.getTimeInMillis(), pIntent);
    }
  }
}
