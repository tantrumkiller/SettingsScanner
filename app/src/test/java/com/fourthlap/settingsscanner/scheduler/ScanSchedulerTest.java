package com.fourthlap.settingsscanner.scheduler;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import com.fourthlap.settingsscanner.TestData;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;
import java.util.Calendar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScanSchedulerTest {

  @Mock
  private AlarmManager alarmManager;

  @Mock
  private Context context;

  @Mock
  private UserPreferences userPreferences;

  @InjectMocks
  private ScanScheduler scanScheduler;

  @Test
  public void verifyAlarmManagerIsInvoked() {
    when(userPreferences.getFrequencyOfScan(any(SharedPreferences.class))).thenReturn(6);
    when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);

    final Calendar scanTime = scanScheduler.scheduleNextScan(context, TestData.getCalendar(2));

    verify(alarmManager, times(1)).setExact(anyInt(), anyLong(), any(PendingIntent.class));

    assertThat(scanTime.get(Calendar.YEAR), is(TestData.DEFAULT_YEAR));
    assertThat(scanTime.get(Calendar.MONTH), is(TestData.DECEMBER));
    assertThat(scanTime.get(Calendar.DAY_OF_MONTH), is(TestData.ELEVENTH));
    assertThat(scanTime.get(Calendar.HOUR_OF_DAY), is(6));
    assertThat(scanTime.get(Calendar.MINUTE), is(0));
    assertThat(scanTime.get(Calendar.SECOND), is(0));
  }
}