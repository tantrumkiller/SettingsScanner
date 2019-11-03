package com.fourthlap.settingsscanner.setting;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SettingsConfigurationTest {

  @Test
  public void validateAllSettingsHaveConfig() {
    final SettingsConfiguration settingsConfiguration = new SettingsConfiguration();
    for (Setting setting : Setting.values()) {
      assertNotNull("Handler missing for setting:" + setting, settingsConfiguration.getHandler(setting));
      assertNotNull("ButtonId missing for setting:" + setting,
          settingsConfiguration.getPreferenceButtonId(setting));
      assertNotNull("Displayable name missing for setting:" + setting,
          settingsConfiguration.getDisplayableNameResId(setting));
    }
  }
}