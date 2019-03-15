package com.arman.queuetube.util.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.arman.queuetube.config.Constants;
import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.R;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static final String NOTIFICATION_CHANNEL_NAME = "Queuetube notifications";
    public static final String NOTIFICATION_CHANNEL_ID = "49210233949201482942101";
    public static final int NOTIFICATION_ID = 666;

    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private RemoteViews views, expandedViews;

    private boolean currentVersionSupportExpandedNotification;
    private boolean built;

    public NotificationHelper(Context context) {
        this.context = context;
        this.currentVersionSupportExpandedNotification = currentVersionSupportExpandedNotification();
    }

    private static boolean currentVersionSupportExpandedNotification() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private void setListeners() {
        Intent playIntent = new Intent();
        playIntent.setAction(Constants.Action.PLAY_ACTION);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent();
        pauseIntent.setAction(Constants.Action.PAUSE_ACTION);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(context, 2, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent();
        nextIntent.setAction(Constants.Action.NEXT_ACTION);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent closeIntent = new Intent();
        closeIntent.setAction(Constants.Action.STOP_ACTION);
        PendingIntent closePendingIntent = PendingIntent.getBroadcast(context, 4, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.status_bar_button_play, playPendingIntent);
        expandedViews.setOnClickPendingIntent(R.id.status_bar_button_play, playPendingIntent);

        views.setOnClickPendingIntent(R.id.status_bar_button_pause, pausePendingIntent);
        expandedViews.setOnClickPendingIntent(R.id.status_bar_button_pause, pausePendingIntent);

        views.setOnClickPendingIntent(R.id.status_bar_button_next, nextPendingIntent);
        expandedViews.setOnClickPendingIntent(R.id.status_bar_button_next, nextPendingIntent);

        views.setOnClickPendingIntent(R.id.status_bar_button_stop, closePendingIntent);
        expandedViews.setOnClickPendingIntent(R.id.status_bar_button_stop, closePendingIntent);
    }

    private void createNotificationBuilder() {
        views = new RemoteViews(context.getPackageName(), R.layout.status_bar);
        expandedViews = new RemoteViews(context.getPackageName(), R.layout.status_bar_expanded);

        this.builder = new NotificationCompat.Builder(context.getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.Action.MAIN_ACTION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        setListeners();

        builder
                .setSmallIcon(R.drawable.ic_stat_name)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
                .setCustomContentView(views)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setDefaults(0);

        if (currentVersionSupportExpandedNotification) {
            builder.setCustomBigContentView(expandedViews);
        }

        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(false);
            channel.enableLights(false);
            channel.setSound(null, null);
            this.notificationManager.createNotificationChannel(channel);
        }
    }

    public void updateNotification(boolean playing) {
        if (playing) {
            views.setViewVisibility(R.id.status_bar_button_play, View.GONE);
            views.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE);

            expandedViews.setViewVisibility(R.id.status_bar_button_play, View.GONE);
            expandedViews.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE);
            views.setViewVisibility(R.id.status_bar_button_pause, View.GONE);

            expandedViews.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE);
            expandedViews.setViewVisibility(R.id.status_bar_button_pause, View.GONE);
        }

        Notification notification = builder.build();
        notification.flags |= NotificationCompat.FLAG_NO_CLEAR;

        assert this.notificationManager != null;
        this.notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void updateNotification(String title) {
        views.setTextViewText(R.id.status_bar_video_name, title);
        expandedViews.setTextViewText(R.id.status_bar_video_name, title);

        Notification notification = builder.setContentTitle(title).build();
        notification.flags |= NotificationCompat.FLAG_NO_CLEAR;

        assert this.notificationManager != null;
        this.notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void updateNotification(String title, boolean playing) {
        views.setTextViewText(R.id.status_bar_video_name, title);
        expandedViews.setTextViewText(R.id.status_bar_video_name, title);

        if (playing) {
            views.setViewVisibility(R.id.status_bar_button_play, View.GONE);
            views.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE);

            expandedViews.setViewVisibility(R.id.status_bar_button_play, View.GONE);
            expandedViews.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE);
            views.setViewVisibility(R.id.status_bar_button_pause, View.GONE);

            expandedViews.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE);
            expandedViews.setViewVisibility(R.id.status_bar_button_pause, View.GONE);
        }

        Notification notification = builder.setContentTitle(title).build();
        notification.flags |= NotificationCompat.FLAG_NO_CLEAR;

        assert this.notificationManager != null;
        this.notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void buildNotification(boolean playing) {
        this.createNotificationBuilder();
        this.updateNotification(playing);
        this.built = true;
    }

    private void buildNotification(String title) {
        this.createNotificationBuilder();
        this.updateNotification(title);
        this.built = true;
    }

    private void buildNotification(String title, boolean playing) {
        this.createNotificationBuilder();
        this.updateNotification(title, playing);
        this.built = true;
    }

    public void updateNotificationIfBuilt(boolean playing) {
        if (built) {
            updateNotification(playing);
        } else {
            buildNotification(playing);
        }
    }

    public void updateNotificationIfBuilt(String title) {
        if (built) {
            updateNotification(title);
        } else {
            buildNotification(title);
        }
    }

    private boolean notificationsEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.enable_notifications_key), false);
    }

    public void updateNotificationIfBuilt(String title, boolean playing) {
        if (this.notificationsEnabled()) {
            if (built) {
                updateNotification(title, playing);
            } else {
                buildNotification(title, playing);
            }
        }
    }

    public static void destroyNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID);
    }

}
