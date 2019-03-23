package com.arman.queuetube.util.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.PlayerFragment

class NotificationReceiver(private val playerFragment: PlayerFragment) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received notification action: " + intent.action!!)
        when (intent.action) {
            Constants.Action.PLAY_ACTION -> this.playerFragment.play()
            Constants.Action.PAUSE_ACTION -> this.playerFragment.pause()
            Constants.Action.NEXT_ACTION -> this.playerFragment.skip()
            Constants.Action.STOP_ACTION -> this.playerFragment.stop()
            else -> {
            }
        }
    }

    companion object {

        val TAG = "NotificationReceiver"
    }

}
