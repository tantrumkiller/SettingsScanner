package com.fourthlap.settingsscanner.setting;

import com.fourthlap.settingsscanner.R;
import java.util.HashMap;
import java.util.Map;

public class SettingsConfig {

  private Map<Setting, Config> settingsMap;

  public SettingsConfig() {
    settingsMap = new HashMap();
    settingsMap.put(Setting.GPS, new Config(new GPSSetting(), R.id.gps_toggle_button,
        R.string.gps_displayable_name));
    settingsMap.put(Setting.MobileData, new Config(new MobileDataSetting(),
        R.id.mobile_data_toggle_button, R.string.mobile_data_displayable_name));
    settingsMap.put(Setting.Bluetooth, new Config(new BluetoothSetting(),
        R.id.bluetooth_toggle_button, R.string.bluetooth_displayable_name));
    settingsMap.put(Setting.HotSpot, new Config(new HotspotSetting(),
        R.id.hotspot_toggle_button, R.string.hotspot_displayable_name));
  }

  public SettingHandler getHandler(Setting setting) {
    return settingsMap.get(setting).handler;
  }

  public int getPreferenceButtonId(Setting setting) {
    return settingsMap.get(setting).preferenceButtonId;
  }

  public int getDisplayableNameId(Setting setting) {
    return settingsMap.get(setting).displayableNameResId;
  }

  private class Config {

    private final SettingHandler handler;
    private final int preferenceButtonId;
    private final int displayableNameResId;

    Config(SettingHandler handler, int preferenceButtonId, int displayableNameResId) {
      this.handler = handler;
      this.preferenceButtonId = preferenceButtonId;
      this.displayableNameResId = displayableNameResId;
    }
  }
}
