package com.fourthlap.settingsscanner.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.lang.reflect.Method;

public class HotspotSetting implements SettingHandler {

  @Override
  public boolean isEnabled(final Context context) {
    // Check if android has detected mobile hotspot
    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    try {
      final Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
      method.setAccessible(true);
      return (Boolean) method.invoke(wifiManager);
    } catch (Exception e) {
      e.printStackTrace();
      Log.i("HotspotSetting", "Exception checking hotspot" + e);
    }

    return false;
  }

  @Override
  public void openSettingsMenu(final Context context) {
    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
    final ComponentName componentName = new ComponentName("com.android.settings",
        "com.android.settings.TetherSettings");

    intent.setComponent(componentName);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
