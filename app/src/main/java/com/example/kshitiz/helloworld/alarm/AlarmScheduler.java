package com.example.kshitiz.helloworld.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.kshitiz.helloworld.TimerBroadcastReceiver;

public class AlarmScheduler {
    private static final long TWO_HRS = AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15;

    public void scheduleAlarm(Context context) {
        final Intent intent = new Intent(context, TimerBroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, TimerBroadcastReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // alarm is set right away
        long firstMillis = System.currentTimeMillis();

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, TWO_HRS, pIntent);
    }
}
