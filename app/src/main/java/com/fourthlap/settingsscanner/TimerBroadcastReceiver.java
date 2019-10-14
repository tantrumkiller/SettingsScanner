package com.fourthlap.settingsscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerBroadcastReceiver extends BroadcastReceiver {
  public static final int REQUEST_CODE = 12345;

  @Override
  public void onReceive(final Context context, final Intent intent) {
    context.startService(new Intent(context, PeriodicTaskExecutorService.class));
  }
}
