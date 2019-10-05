package com.example.kshitiz.helloworld.setting;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class GPSSetting implements SettingHandler {
    @Override
    public boolean isEnabled(final Context context){
        return isLocationEnabled((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
    }

    @Override
    public void openSettingsMenu(final Context context){
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private boolean isLocationEnabled(LocationManager lm) {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            return false;
        }

        return true;
    }
}
