package com.arman.queuetube.model.viewholders

import android.view.View
import android.widget.TextView
import com.arman.queuetube.R
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.BaseTouchAdapter

open class VideoItemViewHolder(view: View) : YouTubeThumbnailViewHolder(view) {

    private val titleView: TextView = view.findViewById(R.id.video_title)

    override fun bind(item: VideoData, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        super.bind(item, clickListener, dragListener)
        this.titleView.text = item.title
    }

    companion object {

        const val TAG = "VideoItemViewHolder"

    }

}
