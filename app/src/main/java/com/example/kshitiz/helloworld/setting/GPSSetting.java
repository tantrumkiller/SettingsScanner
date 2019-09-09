package com.example.kshitiz.helloworld.setting;

import android.content.Context;
import android.location.LocationManager;

public class GPSSetting implements SettingHandler {
    public boolean isEnabled(final Context context){
        return isLocationEnabled((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
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
