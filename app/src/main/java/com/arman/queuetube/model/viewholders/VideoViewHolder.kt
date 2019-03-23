package com.arman.queuetube.model.viewholders

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView

open class VideoViewHolder(view: View) : BaseTouchViewHolder<VideoData>(view) {

    private val thumbnailView: YouTubeThumbnailView
    private val titleView: TextView

    private var readyForLoadingYoutubeThumbnail: Boolean = false

    init {
        this.thumbnailView = view.findViewById<View>(R.id.youtube_thumbnail) as YouTubeThumbnailView
        this.titleView = view.findViewById<View>(R.id.video_title) as TextView
        this.readyForLoadingYoutubeThumbnail = true
    }

    override fun bind(item: VideoData, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        super.bind(item, clickListener, dragListener)
        this.titleView.text = item.title
        if (this.readyForLoadingYoutubeThumbnail) {
            this.readyForLoadingYoutubeThumbnail = false
            this.thumbnailView.initialize(Constants.Key.API_KEY, object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView, youTubeThumbnailLoader: YouTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(item.id)
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                        override fun onThumbnailLoaded(youTubeThumbnailView: YouTubeThumbnailView, s: String) {
                            youTubeThumbnailView.visibility = View.VISIBLE
                            youTubeThumbnailLoader.release()
                        }

                        override fun onThumbnailError(youTubeThumbnailView: YouTubeThumbnailView, errorReason: YouTubeThumbnailLoader.ErrorReason) {
                            youTubeThumbnailView.visibility = View.VISIBLE
                            Toast.makeText(titleView.context, "You don't have an internet connection", Toast.LENGTH_LONG).show()
                            youTubeThumbnailLoader.release()
                        }
                    })
                    this@VideoViewHolder.readyForLoadingYoutubeThumbnail = true
                }

                override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView, youTubeInitializationResult: YouTubeInitializationResult) {

                }
            })
        }
    }

    companion object {

        val TAG = "VideoViewHolder"
    }

}
