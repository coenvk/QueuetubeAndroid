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

    private var view: RemoteViews? = null
    private var expandedView: RemoteViews? = null

    private var built: Boolean = false

    private fun createIntents() {
        val playIntent = Intent(Constants.Action.PLAY_ACTION)
        val playPendingIntent = PendingIntent.getBroadcast(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(Constants.Action.PAUSE_ACTION)
        val pausePendingIntent = PendingIntent.getBroadcast(context, 2, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(Constants.Action.NEXT_ACTION)
        val nextPendingIntent = PendingIntent.getBroadcast(context, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val closeIntent = Intent(Constants.Action.STOP_ACTION)
        val closePendingIntent = PendingIntent.getBroadcast(context, 4, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        view!!.setOnClickPendingIntent(R.id.status_bar_button_play, playPendingIntent)
        expandedView!!.setOnClickPendingIntent(R.id.status_bar_button_play, playPendingIntent)

        view!!.setOnClickPendingIntent(R.id.status_bar_button_pause, pausePendingIntent)
        expandedView!!.setOnClickPendingIntent(R.id.status_bar_button_pause, pausePendingIntent)

        view!!.setOnClickPendingIntent(R.id.status_bar_button_next, nextPendingIntent)
        expandedView!!.setOnClickPendingIntent(R.id.status_bar_button_next, nextPendingIntent)

        view!!.setOnClickPendingIntent(R.id.status_bar_button_stop, closePendingIntent)
        expandedView!!.setOnClickPendingIntent(R.id.status_bar_button_stop, closePendingIntent)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationBuilder() {
        view = RemoteViews(context.packageName, R.layout.status_bar)
        expandedView = RemoteViews(context.packageName, R.layout.status_bar_expanded)

        this.builder = NotificationCompat.Builder(context.applicationContext, Constants.Notification.CHANNEL_ID)

        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.Action.MAIN_ACTION
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        createIntents()

        builder!!
                .setSmallIcon(R.drawable.ic_stat_name)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
                .setCustomContentView(view)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setDefaults(0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder!!.setCustomBigContentView(expandedView)
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

    private fun setPlaying(playing: Boolean) {
        if (playing) {
            view!!.setViewVisibility(R.id.status_bar_button_play, View.GONE)
            view!!.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE)

            expandedView!!.setViewVisibility(R.id.status_bar_button_play, View.GONE)
            expandedView!!.setViewVisibility(R.id.status_bar_button_pause, View.VISIBLE)
        } else {
            view!!.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE)
            view!!.setViewVisibility(R.id.status_bar_button_pause, View.GONE)

            expandedView!!.setViewVisibility(R.id.status_bar_button_play, View.VISIBLE)
            expandedView!!.setViewVisibility(R.id.status_bar_button_pause, View.GONE)
        }
    }

    fun updateNotification(title: String, playing: Boolean) {
        view!!.setTextViewText(R.id.status_bar_video_name, title)
        expandedView!!.setTextViewText(R.id.status_bar_video_name, title)

        this.setPlaying(playing)

        val notification = builder!!.setContentTitle(title).build()
        notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

        assert(this.notificationManager != null)
        this.notificationManager!!.notify(Constants.Notification.ID, notification)
    }

    private fun buildNotification(title: String, playing: Boolean) {
        this.createNotificationBuilder()
        this.updateNotification(title, playing)
        this.built = true
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

        fun destroyNotification(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(Constants.Notification.ID)
        }
    }

}
