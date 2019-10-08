package com.fourthlap.a.settingswatcher.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fourthlap.a.settingswatcher.TimerBroadcastReceiver;

public class AlarmScheduler {
    private static final long THREE_HRS = 3*AlarmManager.INTERVAL_HOUR;

    public void scheduleAlarm(Context context) {
        final Intent intent = new Intent(context, TimerBroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, TimerBroadcastReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // alarm is set right away
        long firstMillis = System.currentTimeMillis();

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, THREE_HRS, pIntent);
    }
}
