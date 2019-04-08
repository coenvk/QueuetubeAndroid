package com.arman.queuetube.listeners

import com.arman.queuetube.listeners.events.PlayEvent
import com.arman.queuetube.model.VideoData

inline fun OnPlayItemsListener(
        crossinline onPlayAll: (Collection<VideoData>) -> Unit = {},
        crossinline onPlay: (VideoData) -> Unit = {},
        crossinline onShuffle: (Collection<VideoData>) -> Unit = {},
        crossinline onPlayNext: (VideoData) -> Unit = {},
        crossinline onPlayNow: (VideoData) -> Unit = {}
): OnPlayItemsListener {
    return object : OnPlayItemsListener {
        override fun onPlayAll(videos: Collection<VideoData>) {
            onPlayAll(videos)
        }

        override fun onPlay(video: VideoData) {
            onPlay(video)
        }

        override fun onShuffle(videos: Collection<VideoData>) {
            onShuffle(videos)
        }

        override fun onPlayNext(video: VideoData) {
            onPlayNext(video)
        }

        override fun onPlayNow(video: VideoData) {
            onPlayNow(video)
        }
    }
}

interface OnPlayItemsListener {

    fun onPlayAll(videos: Collection<VideoData>)

    fun onPlay(video: VideoData)

    fun onShuffle(videos: Collection<VideoData>)

    fun onPlayNext(video: VideoData)

    fun onPlayNow(video: VideoData)

    fun onPlay(videos: Collection<VideoData>, event: PlayEvent) {
        when (event) {
            PlayEvent.PLAY -> onPlay(videos.first())
            PlayEvent.PLAY_NEXT -> onPlayNext(videos.first())
            PlayEvent.PLAY_NOW -> onPlayNow(videos.first())
            PlayEvent.PLAY_ALL -> onPlayAll(videos)
            PlayEvent.SHUFFLE -> onShuffle(videos)
        }
    }

}