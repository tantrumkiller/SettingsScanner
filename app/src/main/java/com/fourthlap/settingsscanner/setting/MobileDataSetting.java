package com.fourthlap.settingsscanner.setting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import java.lang.reflect.Method;

public class MobileDataSetting implements SettingHandler {

  public boolean isEnabled(Context context) {
    boolean mobileDataEnabled = false;

    final ConnectivityManager cm = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    try {
      Class cmClass = Class.forName(cm.getClass().getName());
      Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
      method.setAccessible(true);
      return (Boolean) method.invoke(cm);
    } catch (Exception e) {
      Log.i("Check", "Exception during check for mobile data");
      return mobileDataEnabled;
    }
  }

  public void openSettingsMenu(Context context) {
    final Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
