package com.arman.queuetube.fragments.main

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.dialogs.AddToPlaylistFragment
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.VideoItemAdapter
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.modules.search.YouTubeSearcher
import com.arman.queuetube.receivers.NotificationReceiver
import com.arman.queuetube.util.VideoSharer
import com.arman.queuetube.util.notifications.NotificationHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.player.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker

class PlayerFragment : Fragment(), YouTubePlayerInitListener {

    private var broadcastReceiver: BroadcastReceiver? = null

    private var contentFrame: View? = null

    private var playerFrame: View? = null
    private var openButton: ImageView? = null
    private var isUp: Boolean = false
    private var swipeHeight: Float = 0f

    private var videoTitleView: TextView? = null

    private var favoriteButton: ImageView? = null
    private var addToPlaylistButton: ImageView? = null
    private var shareButton: ImageView? = null

    private var ytPlayerView: YouTubePlayerView? = null
    private var ytPlayer: YouTubePlayer? = null
    private var ytPlayerTracker: YouTubePlayerTracker? = null
    private var ytPlayerReady: Boolean = false
    private var ytPlayerVideoSet: Boolean = false
    private var ytPlayerPlaying: Boolean = false
    private var ytPlayerStopped: Boolean = false

    var currentVideo: VideoData? = null
        private set

    private var queueFragment: QueueFragment? = null

    private var notificationHelper: NotificationHelper? = null

    private val isAutoplayEnabled: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(getString(R.string.enable_autoplay_key), false)

    private val playlistAdapter: VideoItemAdapter
        get() = this.queueFragment?.listAdapter!!

    private fun setupReceiver() {
        this.broadcastReceiver = NotificationReceiver(this)
        val filter = IntentFilter()
        filter.addAction(Constants.Action.NEXT_ACTION)
        filter.addAction(Constants.Action.MAIN_ACTION)
        filter.addAction(Constants.Action.PAUSE_ACTION)
        filter.addAction(Constants.Action.PLAY_ACTION)
        filter.addAction(Constants.Action.STOP_ACTION)
        activity!!.registerReceiver(this.broadcastReceiver, filter)
    }

    fun swipeUp() {
        if (!isUp) {
            this.contentFrame!!.visibility = View.VISIBLE
            view!!.translationY = 0f
            openButton!!.setImageResource(R.drawable.ic_chevron_down_white_36dp)
            isUp = true
        }
    }

    fun swipeDown() {
        if (isUp) {
            view!!.translationY = swipeHeight
            contentFrame!!.visibility = View.GONE
            openButton!!.setImageResource(R.drawable.ic_chevron_up_white_36dp)
            isUp = false
        }
    }

    private fun setupPlayerFrame() {
        this.openButton = view!!.findViewById(R.id.player_bar_open_button) as ImageView
        this.contentFrame = view!!.findViewById(R.id.player_content_frame) as View

        this.swipeHeight = this.contentFrame!!.height.toFloat()
        this.contentFrame!!.visibility = View.GONE

        this.openButton!!.setOnClickListener {
            if (isUp) {
                swipeDown()
            } else {
                swipeUp()
            }
        }

        view!!.visibility = View.VISIBLE
    }

    fun setQueueTo(videoData: Collection<VideoData>): Boolean {
        return this.playlistAdapter.setAll(videoData)
    }

    fun addToQueue(videoData: Collection<VideoData>): Boolean {
        return this.playlistAdapter.addAll(videoData)
    }

    fun addToQueue(video: VideoData): Boolean {
        return this.playlistAdapter.add(video)
    }

    fun addToQueue(index: Int, video: VideoData): Boolean {
        return this.playlistAdapter.add(index, video)
    }

    fun showAddToPlaylistDialog() {
        val dialog = AddToPlaylistFragment()
        dialog.video = this.currentVideo!!
        dialog.show(fragmentManager!!, "add_to_playlist_dialog")
    }

    override fun onDestroy() {
        super.onDestroy()
        this.ytPlayerView?.release()
        activity!!.unregisterReceiver(this.broadcastReceiver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = IFramePlayerOptions.Builder().controls(0).autoplay(1).modestBranding(1).ivLoadPolicy(3).rel(0).build()

        this.ytPlayerView = view.findViewById(R.id.youtube_player) as YouTubePlayerView
        this.ytPlayerView!!.initialize(this, true, options)

        this.currentVideo = VideoData()

        this.playerFrame = view.findViewById(R.id.player_fragment_layout)
        this.videoTitleView = this.playerFrame!!.findViewById(R.id.video_title_text_view)

        this.favoriteButton = this.playerFrame!!.findViewById(R.id.favorite_button) as ImageView
        this.addToPlaylistButton = this.playerFrame!!.findViewById(R.id.add_to_playlist_button) as ImageView
        this.shareButton = this.playerFrame!!.findViewById(R.id.share_button) as ImageView

        this.favoriteButton!!.setOnClickListener { this@PlayerFragment.favoriteVideo(!this@PlayerFragment.currentVideo!!.isFavorited) }

        this.addToPlaylistButton!!.setOnClickListener { this@PlayerFragment.showAddToPlaylistDialog() }

        this.shareButton!!.setOnClickListener { VideoSharer.share(context!!, this@PlayerFragment.currentVideo!!) }

        val bundle = Bundle()
        bundle.putBoolean(Constants.Fragment.Argument.IS_DRAGGABLE, true)
        bundle.putBoolean(Constants.Fragment.Argument.IS_SORTABLE, false)
        bundle.putBoolean(Constants.Fragment.Argument.IS_SHUFFLABLE, false)
        this.queueFragment = QueueFragment()
        this.queueFragment!!.arguments = bundle
        fragmentManager?.beginTransaction()?.add(R.id.queue_content_frame, this.queueFragment!!)?.commit()

        setupPlayerFrame()

        this.setupReceiver()
        this.notificationHelper = NotificationHelper(activity!!)
    }

    fun updateVideo(favorited: Boolean) {
        this.currentVideo?.isFavorited = favorited
        this.adjustFavoriteButton(favorited)
    }

    private fun favoriteVideo(favorited: Boolean) {
        if (favorited) {
            GsonPlaylistHelper.writeTo(Constants.Json.Playlist.FAVORITES, this.currentVideo!!)
        } else {
            GsonPlaylistHelper.removeFrom(Constants.Json.Playlist.FAVORITES, this.currentVideo!!)
        }

        this.updateVideo(favorited)
    }

    private fun adjustFavoriteButton(favorited: Boolean) {
        if (favorited) {
            this.favoriteButton?.setImageDrawable(activity!!.getDrawable(R.drawable.ic_heart_white_36dp))
        } else {
            this.favoriteButton?.setImageDrawable(activity!!.getDrawable(R.drawable.ic_heart_outline_white_36dp))
        }
    }

    private fun shouldSaveHistory(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(getString(R.string.save_history_key), true)
    }

    fun forcePlayNext(): Boolean {
        var ret = false
        if (this.ytPlayerReady && this.ytPlayerVideoSet) {
            this.skip()
            if (this.playlistAdapter.isNotEmpty) {
                this.queueFragment?.showList()
            }
        } else {
            ret = this.tryPlayNext()
        }
        return ret
    }

    fun tryPlayNext(): Boolean {
        return this.tryPlayNext(true)
    }

    fun tryPlayNext(autoplayIfEnabled: Boolean): Boolean {
        var ret = false
        if (this.ytPlayerReady && !this.ytPlayerVideoSet) {
            if (!this.playlistAdapter.isEmpty) {
                val nextVideo = this.playlistAdapter.pop()
                this.currentVideo?.setTo(nextVideo!!)
                this.ytPlayer?.cueVideo(this.currentVideo?.id!!, 0f)
                ret = true
            } else if (this.isAutoplayEnabled && autoplayIfEnabled) {
                var nextVideo = YouTubeSearcher.nextAutoplay(this.currentVideo?.id!!)
                if (nextVideo != null) {
                    this.currentVideo?.setTo(nextVideo)
                    this.ytPlayer?.cueVideo(this.currentVideo?.id!!, 0f)
                    ret = true
                }
            }
        }
        if (this.playlistAdapter.isNotEmpty) {
            this.queueFragment?.showList()
        }
        return ret
    }

    fun playNext() {
        if (!tryPlayNext(!this.ytPlayerStopped)) {
            this.playerFrame?.visibility = View.GONE
            NotificationHelper.destroyNotification(context!!)
        }
    }

    fun stop(autoplayIfEnabled: Boolean = false) {
        this.queueFragment?.listAdapter?.clear()
        this.ytPlayerStopped = !autoplayIfEnabled
        this.ytPlayer?.seekTo(this.ytPlayerTracker!!.videoDuration)
    }

    fun play() {
        this.ytPlayer?.play()
    }

    fun pause() {
        this.ytPlayer?.pause()
    }

    fun skip() {
        if (this.ytPlayer != null) {
            if (this.playlistAdapter.isEmpty) {
                this.stop(true)
            } else {
                onEnd()
            }
        }
    }

    private fun onEnd() {
        if (this.shouldSaveHistory()) {
            GsonPlaylistHelper.writeToOrReorder(Constants.Json.Playlist.HISTORY, this.currentVideo!!, 0)
        }

        this.ytPlayerVideoSet = false

        this.playNext()
        if (this.playlistAdapter.isEmpty) {
            this.queueFragment?.showEmptyText()
        }

        this.ytPlayerStopped = false
    }

    private fun onPlaying() {
        this.notificationHelper?.updateNotificationIfBuilt(this.currentVideo!!.title!!, true)
        this.ytPlayerPlaying = true
    }

    private fun onPaused() {
        this.notificationHelper?.updateNotificationIfBuilt(this.currentVideo!!.title!!, false)
        this.ytPlayerPlaying = false
    }

    private fun onVideoCued() {
        //        this.currentVideo.setTo(this.ytSearcher.requestDetails(this.currentVideo));

        val favorited = GsonPlaylistHelper.isFavorited(this.currentVideo!!)
        this.currentVideo?.isFavorited = favorited
        this.adjustFavoriteButton(favorited)
        this.videoTitleView?.text = this.currentVideo?.title
        if (this.playerFrame?.visibility == View.GONE) {
            this.playerFrame?.visibility = View.VISIBLE
        }
        this.ytPlayer?.play()
        this.notificationHelper?.updateNotificationIfBuilt(this.currentVideo?.title!!, true)
        this.ytPlayerVideoSet = true
    }

    override fun onInitSuccess(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer) {
        this.ytPlayer = youTubePlayer
        this.ytPlayerTracker = YouTubePlayerTracker()
        this.ytPlayer!!.addListener(this.ytPlayerTracker!!)
        this.ytPlayer!!.addListener(object : YouTubePlayerListener {
            override fun onReady() {
                this@PlayerFragment.ytPlayerView!!.enableBackgroundPlayback(true)

                val uiController = this@PlayerFragment.ytPlayerView!!.playerUIController
                uiController.setCustomAction2(activity!!.getDrawable(R.drawable.ic_skip_next_white_36dp)!!) { this@PlayerFragment.skip() }
                this@PlayerFragment.ytPlayerReady = true
            }

            override fun onStateChange(state: PlayerConstants.PlayerState) {
                when (state) {
                    PlayerConstants.PlayerState.UNKNOWN -> {
                    }
                    PlayerConstants.PlayerState.UNSTARTED -> {
                    }
                    PlayerConstants.PlayerState.ENDED -> onEnd()
                    PlayerConstants.PlayerState.PLAYING -> onPlaying()
                    PlayerConstants.PlayerState.PAUSED -> onPaused()
                    PlayerConstants.PlayerState.BUFFERING -> {
                    }
                    PlayerConstants.PlayerState.VIDEO_CUED -> onVideoCued()
                    else -> {
                    }
                }
            }

            override fun onPlaybackQualityChange(playbackQuality: PlayerConstants.PlaybackQuality) {

            }

            override fun onPlaybackRateChange(playbackRate: PlayerConstants.PlaybackRate) {

            }

            override fun onError(error: PlayerConstants.PlayerError) {
                this@PlayerFragment.skip()
            }

            override fun onApiChange() {

            }

            override fun onCurrentSecond(second: Float) {

            }

            override fun onVideoDuration(duration: Float) {

            }

            override fun onVideoLoadedFraction(loadedFraction: Float) {

            }

            override fun onVideoId(videoId: String) {

            }
        })
    }

}
