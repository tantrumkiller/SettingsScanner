package com.fourthlap.a.settingswatcher.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.fourthlap.a.settingswatcher.ActionsRequiredActivity;
import com.fourthlap.a.settingswatcher.R;

public class ReminderNotificationHandler {
    public static final String NOTIFICATION_CHANNEL_ID = "SettingChecker-1";
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_NAME = "SettingChecker";

    public void createNotificationChannel(final NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification Channel for SettingsChecker App");

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notifyUser(final Context context, final NotificationManager notificationManager) {
        final Intent notificationIntent = new Intent(context, ActionsRequiredActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_access_time)
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancelNotification(final NotificationManager notificationManager){
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
