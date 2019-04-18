package com.arman.queuetube.model.viewholders

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import com.arman.queuetube.R
import com.arman.queuetube.model.Video
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

open class ImageViewHolder(view: View) : BaseTouchViewHolder<Video>(view) {

    protected val imageView: ImageView = view.findViewById(R.id.youtube_thumbnail)

    protected var readyForLoadingYoutubeThumbnail: Boolean = true

    override fun bind(item: Video, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        super.bind(item, clickListener, dragListener)

        if (this.readyForLoadingYoutubeThumbnail) {
            this.readyForLoadingYoutubeThumbnail = false

            Picasso.get()
                    .load(item.highThumbnailUrl)
                    .placeholder(ColorDrawable(Color.GRAY))
                    .error(ColorDrawable(Color.RED))
                    .fit()
                    .centerCrop()
                    .into(this.imageView, object : Callback {
                        override fun onSuccess() {
                            this@ImageViewHolder.readyForLoadingYoutubeThumbnail = true
                        }

                        override fun onError(e: Exception?) {
                            this@ImageViewHolder.readyForLoadingYoutubeThumbnail = true
                        }
                    })
        }
    }

    companion object {

        const val TAG = "ImageViewHolder"

    }

}