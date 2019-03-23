package com.arman.queuetube.util.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.JobIntentService
import com.arman.queuetube.config.Constants

class KillNotificationService : JobIntentService() {

    private val binder: KillBinder

    init {
        this.binder = KillBinder(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return this.binder
    }

    override fun onHandleWork(intent: Intent) {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    private fun clearNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(Constants.Notification.ID)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        clearNotification()
    }

    inner class KillBinder(var service: Service) : Binder()

}
