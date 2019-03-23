package com.arman.queuetube.util.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.arman.queuetube.R
import com.arman.queuetube.activities.MainActivity
import com.arman.queuetube.config.Constants

class NotificationHelper(private val context: Context) {
    private var notificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null

    private var views: RemoteViews? = null
    private var expandedViews: RemoteViews? = null

    private val currentVersionSupportExpandedNotification: Boolean
    private var built: Boolean = false

    init {
        this.currentVersionSupportExpandedNotification = currentVersionSupportExpandedNotification()
    }

    private fun setListeners() {
        val playIntent = Intent()
        playIntent.action = Constants.Action.PLAY_ACTION
        val playPendingIntent = PendingIntent.getBroadcast(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent()
        pauseIntent.action = Constants.Action.PAUSE_ACTION
        val pausePendingIntent = PendingIntent.getBroadcast(context, 2, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent()
        nextIntent.action = Constants.Action.NEXT_ACTION
        val nextPendingIntent = PendingIntent.getBroadcast(context, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val closeIntent = Intent()
        closeIntent.action = Constants.Action.STOP_ACTION
        val closePendingIntent = PendingIntent.getBroadcast(context, 4, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        views!!.setOnClickPendingIntent(R.id.status_bar_button_play, playPendingIntent)
        expandedViews!!.setOnClickPendingIntent(R.id.status_bar_button_play, playPendingIntent)

        views!!.setOnClickPendingIntent(R.id.status_bar_button_pause, pausePendingIntent)
        expandedViews!!.setOnClickPendingIntent(R.id.status_bar_button_pause, pausePendingIntent)

        views!!.setOnClickPendingIntent(R.id.status_bar_button_next, nextPendingIntent)
        expandedViews!!.setOnClickPendingIntent(R.id.status_bar_button_next, nextPendingIntent)

        views!!.setOnClickPendingIntent(R.id.status_bar_button_stop, closePendingIntent)
        expandedViews!!.setOnClickPendingIntent(R.id.status_bar_button_stop, closePendingIntent)
    }

    private fun createNotificationBuilder() {
        views = RemoteViews(context.packageName, R.layout.status_bar)
        expandedViews = RemoteViews(context.packageName, R.layout.status_bar_expanded)

        this.builder = NotificationCompat.Builder(context.applicationContext, Constants.Notification.CHANNEL_ID)

        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.Action.MAIN_ACTION
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        setListeners()

        builder!!
                .setSmallIcon(R.drawable.ic_stat_name)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
                .setCustomContentView(views)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setDefaults(0)

        if (currentVersionSupportExpandedNotification) {
            builder!!.setCustomBigContentView(expandedViews)
        }

        this.notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.Notification.CHANNEL_ID, Constants.Notification.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(false)
            channel.enableLights(false)
            channel.setSound(null, null)
            this.notificationManager!!.createNotificationChannel(channel)
        }
    }

    fun updateNotification(playing: Boolean) {
        if (playing) {
            views!!.setViewVisibility(R.id.status_bar_button_play, View.GONE)
            views!!.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE)

            expandedViews!!.setViewVisibility(R.id.status_bar_button_play, View.GONE)
            expandedViews!!.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE)
        } else {
            views!!.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE)
            views!!.setViewVisibility(R.id.status_bar_button_pause, View.GONE)

            expandedViews!!.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE)
            expandedViews!!.setViewVisibility(R.id.status_bar_button_pause, View.GONE)
        }

        val notification = builder!!.build()
        notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

        assert(this.notificationManager != null)
        this.notificationManager!!.notify(Constants.Notification.ID, notification)
    }

    fun updateNotification(title: String) {
        views!!.setTextViewText(R.id.status_bar_video_name, title)
        expandedViews!!.setTextViewText(R.id.status_bar_video_name, title)

        val notification = builder!!.setContentTitle(title).build()
        notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

        assert(this.notificationManager != null)
        this.notificationManager!!.notify(Constants.Notification.ID, notification)
    }

    fun updateNotification(title: String, playing: Boolean) {
        views!!.setTextViewText(R.id.status_bar_video_name, title)
        expandedViews!!.setTextViewText(R.id.status_bar_video_name, title)

        if (playing) {
            views!!.setViewVisibility(R.id.status_bar_button_play, View.GONE)
            views!!.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE)

            expandedViews!!.setViewVisibility(R.id.status_bar_button_play, View.GONE)
            expandedViews!!.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE)
        } else {
            views!!.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE)
            views!!.setViewVisibility(R.id.status_bar_button_pause, View.GONE)

            expandedViews!!.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE)
            expandedViews!!.setViewVisibility(R.id.status_bar_button_pause, View.GONE)
        }

        val notification = builder!!.setContentTitle(title).build()
        notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

        assert(this.notificationManager != null)
        this.notificationManager!!.notify(Constants.Notification.ID, notification)
    }

    private fun buildNotification(playing: Boolean) {
        this.createNotificationBuilder()
        this.updateNotification(playing)
        this.built = true
    }

    private fun buildNotification(title: String) {
        this.createNotificationBuilder()
        this.updateNotification(title)
        this.built = true
    }

    private fun buildNotification(title: String, playing: Boolean) {
        this.createNotificationBuilder()
        this.updateNotification(title, playing)
        this.built = true
    }

    fun updateNotificationIfBuilt(playing: Boolean) {
        if (built) {
            updateNotification(playing)
        } else {
            buildNotification(playing)
        }
    }

    fun updateNotificationIfBuilt(title: String) {
        if (built) {
            updateNotification(title)
        } else {
            buildNotification(title)
        }
    }

    private fun notificationsEnabled(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.enable_notifications_key), false)
    }

    fun updateNotificationIfBuilt(title: String, playing: Boolean) {
        if (this.notificationsEnabled()) {
            if (built) {
                updateNotification(title, playing)
            } else {
                buildNotification(title, playing)
            }
        }
    }

    companion object {

        @SuppressLint("ObsoleteSdkInt")
        private fun currentVersionSupportExpandedNotification(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        }

        fun destroyNotification(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(Constants.Notification.ID)
        }
    }

}
