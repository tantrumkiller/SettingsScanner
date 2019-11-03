package com.fourthlap.settingsscanner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.scheduler.ScanScheduler;
import com.fourthlap.settingsscanner.setting.SettingsScanner;
import com.fourthlap.settingsscanner.userpreference.TimeOfTheDay;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PeriodicScanExecutorServiceTest {

  @Mock
  private ReminderNotificationHandler reminderNotificationHandler;
  @Mock
  private SettingsScanner settingsScanner;
  @Mock
  private ScanScheduler scanScheduler;
  @Mock
  private UserPreferences userPreferences;

  @Mock
  private Context context;

  private PeriodicScanExecutorService periodicScanExecutor;

  @Before
  public void setUp() {
    periodicScanExecutor = new PeriodicScanExecutorService(reminderNotificationHandler,
        settingsScanner, scanScheduler, userPreferences);
    when(userPreferences.getSleepWindowStartTime(any(SharedPreferences.class)))
        .thenReturn(new TimeOfTheDay(8, 0));
    when(userPreferences.getSleepWindowEndTime(any(SharedPreferences.class)))
        .thenReturn(new TimeOfTheDay(12, 0));
  }

  @Test
  public void testSleepTime_noNotification() {
    periodicScanExecutor.executePeriodicScan(context, TestData.getCalendar(10));

    verifyZeroInteractions(reminderNotificationHandler);
  }

  @Test
  public void testAllSettingsDisabled_noNotification() {
    when(settingsScanner.isAnySettingEnabled(any(Context.class))).thenReturn(false);
    periodicScanExecutor.executePeriodicScan(context, TestData.getCalendar(15));

    verify(reminderNotificationHandler, times(1))
        .cancelNotification(any(NotificationManager.class));
  }

  @Test
  public void testSettingsEnabledOutsideSleepTime_sendsNotification() {
    when(settingsScanner.isAnySettingEnabled(any(Context.class))).thenReturn(true);
    periodicScanExecutor.executePeriodicScan(context, TestData.getCalendar(15));

    verify(reminderNotificationHandler, times(1))
        .notifyUser(any(Context.class), any(NotificationManager.class));
  }

}