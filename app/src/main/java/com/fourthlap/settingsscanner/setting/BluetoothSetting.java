package com.fourthlap.settingsscanner.setting;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class BluetoothSetting implements SettingHandler {

  @Override
  public boolean isEnabled(final Context context) {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter == null) {
      Log.i("BluetoothSetting", "Device doesn't have Bluetooth");
      return false;
    } else if (!mBluetoothAdapter.isEnabled()) {
      return false;
    }

    return true;
  }

  @Override
  public void openSettingsMenu(final Context context) {
    final Intent intentOpenBluetoothSettings = new Intent();
    intentOpenBluetoothSettings.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
    intentOpenBluetoothSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentOpenBluetoothSettings);
  }
}
