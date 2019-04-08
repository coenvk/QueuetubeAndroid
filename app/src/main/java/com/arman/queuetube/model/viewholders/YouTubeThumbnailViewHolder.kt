package com.arman.queuetube.model.viewholders

import android.graphics.drawable.Drawable
import android.os.DeadObjectException
import android.view.View
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView

open class YouTubeThumbnailViewHolder(view: View) : BaseTouchViewHolder<VideoData>(view) {

    protected val thumbnailView: YouTubeThumbnailView = view.findViewById(R.id.youtube_thumbnail)
    protected var thumbnailLoader: YouTubeThumbnailLoader? = null
    protected var image: Drawable? = null

    protected var readyForLoadingYoutubeThumbnail: Boolean = false

    init {
        this.thumbnailView.setTag(R.id.initialize, UNINITIALIZED)
        this.readyForLoadingYoutubeThumbnail = true
        this.initialize()
    }

    private fun initialize() {
        if (this.readyForLoadingYoutubeThumbnail) {
            this.readyForLoadingYoutubeThumbnail = false

            this.thumbnailView.setTag(R.id.initialize, INITIALIZING)
            this.thumbnailView.setTag(R.id.thumbnailloader, null)
            this.thumbnailView.setTag(R.id.videoId, "")

            this.thumbnailView.initialize(Constants.Key.API_KEY, object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView, youTubeThumbnailLoader: YouTubeThumbnailLoader) {
                    thumbnailView.setTag(R.id.initialize, INITIALIZED)
                    if (thumbnailLoader != null) {
                        return
                    }
                    thumbnailLoader = youTubeThumbnailLoader

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                        override fun onThumbnailLoaded(youTubeThumbnailView: YouTubeThumbnailView, s: String) {
                            if (youTubeThumbnailView.drawable != null) {
                                image = youTubeThumbnailView.drawable
                            }
                        }

                        override fun onThumbnailError(youTubeThumbnailView: YouTubeThumbnailView, errorReason: YouTubeThumbnailLoader.ErrorReason) {
                            youTubeThumbnailView.setImageDrawable(null)
                        }
                    })

                    val videoId = youTubeThumbnailView.getTag(R.id.videoId) as String?
                    if (videoId?.isNotEmpty()!!) {
                        youTubeThumbnailLoader.setVideo(videoId)
                    }
                }

                override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView, youTubeInitializationResult: YouTubeInitializationResult) {
                    youTubeThumbnailView.setTag(R.id.initialize, UNINITIALIZED)
                    readyForLoadingYoutubeThumbnail = true
                }
            })
        }
    }

    override fun bind(item: VideoData, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        super.bind(item, clickListener, dragListener)
        this.thumbnailView.setImageDrawable(this.image)
        this.thumbnailView.setTag(R.id.videoId, item.id)

        val state = this.thumbnailView.getTag(R.id.initialize)
        when (state) {
            UNINITIALIZED -> this.initialize()
            INITIALIZED -> {
                // TODO deal with leaking service connection
                try {
                    this.thumbnailLoader?.setVideo(item.id)
                } catch (e: IllegalStateException) {
                    this.thumbnailLoader = null
                    this.initialize()
                } catch (e: DeadObjectException) {
                    this.thumbnailLoader = null
                    this.initialize()
                }
            }
        }
    }

    override fun unbind() {
        super.unbind()
        this.thumbnailLoader?.release()
    }

    companion object {

        const val TAG = "YouTubeThumbnailViewHolder"

        const val UNINITIALIZED = 1
        const val INITIALIZING = 2
        const val INITIALIZED = 3
    }

}