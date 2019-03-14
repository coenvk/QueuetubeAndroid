package com.arman.queuetube.util.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.arman.queuetube.util.notifications.NotificationHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

public class KillNotificationService extends JobIntentService {

    public class KillBinder extends Binder {

        public Service service;

        public KillBinder(Service service) {
            this.service = service;
        }

    }

    private KillBinder binder;

    public KillNotificationService() {
        this.binder = new KillBinder(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        clearNotification();
    }

}
