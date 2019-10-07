package com.fourthlap.a.settingspatroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fourthlap.a.settingspatroller.alarm.AlarmScheduler;

public class DeviceBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmScheduler().scheduleAlarm(context);
        }
    }
}