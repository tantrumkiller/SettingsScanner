package com.fourthlap.a.settingspatroller.userpreference;

import android.content.SharedPreferences;

import com.fourthlap.a.settingspatroller.setting.Setting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserPreferencesStore {
    public Set<Setting> getSettingsEnabledForWatch(final SharedPreferences sharedPref) {
        final Set<Setting> whitelistedSettings = new HashSet<>();

        for(Setting setting: Setting.values()){
            boolean isEnabled = sharedPref.getBoolean(setting.name(), true);
            if(isEnabled){
                whitelistedSettings.add(setting);
            }
        }

        return Collections.unmodifiableSet(whitelistedSettings);
    }

    public void updateSettingPreference(final SharedPreferences sharedPref, final Setting setting, boolean isEnabled) {
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(setting.name(), isEnabled);
        editor.commit();
    }
}
