package com.fourthlap.settingsscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.fourthlap.settingsscanner.scheduler.ScanScheduler;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;

public class DeviceBootBroadcastReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
      Log.i("DeviceBootBroadcast", "Boot event received, setting initial alarm");
      new ScanScheduler(new UserPreferencesStore()).scheduleNextScan(context);
    }
  }
}