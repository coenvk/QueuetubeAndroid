package com.arman.queuetube.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arman.queuetube.config.Constants
import com.arman.queuetube.listeners.OnPlayItemsListener
import com.arman.queuetube.model.VideoData

class PlayReceiver(private val onPlayItemsListener: OnPlayItemsListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val videos = intent.getParcelableArrayListExtra<VideoData>(Constants.Fragment.Argument.VIDEO_LIST)
        when (intent.action) {
            Constants.Action.Play.PLAY_ACTION -> this.onPlayItemsListener.onPlay(videos.first())
            Constants.Action.Play.PLAY_NEXT_ACTION -> this.onPlayItemsListener.onPlayNext(videos.first())
            Constants.Action.Play.PLAY_NOW_ACTION -> this.onPlayItemsListener.onPlayNow(videos.first())
            Constants.Action.Play.PLAY_ALL_ACTION -> this.onPlayItemsListener.onPlayAll(videos)
            Constants.Action.Play.SHUFFLE_ACTION -> this.onPlayItemsListener.onShuffle(videos)
            else -> {
            }
        }
    }

    companion object {

        const val TAG = "PlayReceiver"

    }

}