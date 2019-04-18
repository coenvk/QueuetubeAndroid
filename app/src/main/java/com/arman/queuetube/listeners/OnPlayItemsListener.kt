package com.arman.queuetube.listeners

import com.arman.queuetube.listeners.events.PlayEvent
import com.arman.queuetube.model.Video

inline fun OnPlayItemsListener(
        crossinline onPlayAll: (Collection<Video>) -> Unit = {},
        crossinline onPlay: (Video) -> Unit = {},
        crossinline onShuffle: (Collection<Video>) -> Unit = {},
        crossinline onPlayNext: (Video) -> Unit = {},
        crossinline onPlayNow: (Video) -> Unit = {}
): OnPlayItemsListener {
    return object : OnPlayItemsListener {
        override fun onPlayAll(videos: Collection<Video>) {
            onPlayAll(videos)
        }

        override fun onPlay(video: Video) {
            onPlay(video)
        }

        override fun onShuffle(videos: Collection<Video>) {
            onShuffle(videos)
        }

        override fun onPlayNext(video: Video) {
            onPlayNext(video)
        }

        override fun onPlayNow(video: Video) {
            onPlayNow(video)
        }
    }
}

interface OnPlayItemsListener {

    fun onPlayAll(videos: Collection<Video>)

    fun onPlay(video: Video)

    fun onShuffle(videos: Collection<Video>)

    fun onPlayNext(video: Video)

    fun onPlayNow(video: Video)

    fun onPlay(videos: Collection<Video>, event: PlayEvent) {
        when (event) {
            PlayEvent.PLAY -> onPlay(videos.first())
            PlayEvent.PLAY_NEXT -> onPlayNext(videos.first())
            PlayEvent.PLAY_NOW -> onPlayNow(videos.first())
            PlayEvent.PLAY_ALL -> onPlayAll(videos)
            PlayEvent.SHUFFLE -> onShuffle(videos)
        }
    }

}