package com.example.kshitiz.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerBroadcastReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent i = new Intent(context, PeriodicTaskExecutorService.class);
        context.startService(i);
    }
}
