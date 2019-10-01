package com.example.kshitiz.helloworld.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.lang.reflect.Method;

public class MobileDataSetting implements SettingHandler {
    public boolean isEnabled(Context context){
        boolean mobileDataEnabled = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            return (Boolean)method.invoke(cm);
        } catch (Exception e) {
            Log.i("Check", "Exception during check for mobile data");
            return mobileDataEnabled;
        }
    }

    public void openSettingsMenu(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
        context.startActivity(intent);
    }
}
