package com.arman.queuetube.util.notifications.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.PlayerFragment;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String TAG = "NotificationReceiver";

    private PlayerFragment playerFragment;

    public NotificationReceiver(PlayerFragment playerFragment) {
        this.playerFragment = playerFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received notification action: " + intent.getAction());
        switch (intent.getAction()) {
            case Constants.Action.PLAY_ACTION:
                this.playerFragment.play();
                break;
            case Constants.Action.PAUSE_ACTION:
                this.playerFragment.pause();
                break;
            case Constants.Action.NEXT_ACTION:
                this.playerFragment.skip();
                break;
            case Constants.Action.STOP_ACTION:
                this.playerFragment.stop();
                break;
            default:
                break;
        }
    }

}
