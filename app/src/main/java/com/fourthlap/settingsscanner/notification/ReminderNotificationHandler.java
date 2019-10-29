package com.fourthlap.settingsscanner.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.fourthlap.settingsscanner.ActionsRequiredActivity;
import com.fourthlap.settingsscanner.R;

public class ReminderNotificationHandler {
  private static final String NOTIFICATION_CHANNEL_ID = "SettingChecker-1";
  private static final int NOTIFICATION_ID = 1;
  private static final String NOTIFICATION_CHANNEL_NAME = "SettingChecker";

  public void notifyUser(final Context context, final NotificationManager notificationManager) {
    createNotificationChannel(notificationManager);

    final Intent notificationIntent = new Intent(context, ActionsRequiredActivity.class);

    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    final PendingIntent intent = PendingIntent.getActivity(context, 0,
        notificationIntent, 0);

    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
        NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_stat_access_time)
        .setContentTitle(context.getString(R.string.reminder_notification_title))
        .setContentText(context.getString(R.string.reminder_notification_text))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(intent)
        .setOngoing(true);

    notificationManager.notify(NOTIFICATION_ID, builder.build());
  }

  public void cancelNotification(final NotificationManager notificationManager) {
    notificationManager.cancel(NOTIFICATION_ID);
  }

  private void createNotificationChannel(final NotificationManager notificationManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      final NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
          NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
      channel.setDescription("Notification Channel for SettingsScanner App");

      notificationManager.createNotificationChannel(channel);
    }
  }
}
