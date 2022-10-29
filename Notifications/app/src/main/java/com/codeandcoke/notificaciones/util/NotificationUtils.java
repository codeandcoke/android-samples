package com.codeandcoke.notificaciones.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.codeandcoke.notificaciones.R;

/**
 * Utility methods about notifications
 */
public class NotificationUtils {

    public static void createNotificationChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String title, String text, String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_home_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }

    // Notification + Activity launcher
    public static void showNotification(Context context, int titleId, int textId, String channelId, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_home_foreground)
                .setContentTitle(context.getString(titleId))
                .setContentText(context.getString(textId))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }
}
