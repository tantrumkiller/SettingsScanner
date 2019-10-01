package com.example.kshitiz.helloworld.setting;

import com.example.kshitiz.helloworld.R;

import java.util.HashMap;
import java.util.Map;

public class SettingsConfig {
    private Map<Setting, Config> settingsMap;

    public SettingsConfig() {
        settingsMap = new HashMap();
        settingsMap.put(Setting.GPS, new Config(new GPSSetting(), R.id.gps_toggle_button));
        settingsMap.put(Setting.MobileData, new Config(new MobileDataSetting(), R.id.mobile_data_toggle_button));
    }

    public SettingHandler getHandler(Setting setting) {
        return settingsMap.get(setting).handler;
    }

    public int getPreferenceButtonId(Setting setting) {
        return settingsMap.get(setting).preferenceButtonId;
    }


    private class Config {
        private final SettingHandler handler;
        private final int preferenceButtonId;

        Config(SettingHandler handler, int preferenceButtonId) {
            this.handler = handler;
            this.preferenceButtonId = preferenceButtonId;
        }
    }
}
