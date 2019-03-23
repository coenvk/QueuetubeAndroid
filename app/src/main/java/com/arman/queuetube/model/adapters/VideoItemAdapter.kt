package com.arman.queuetube.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arman.queuetube.R
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.viewholders.VideoViewHolder

open class VideoItemAdapter : BaseTouchAdapter<VideoData, VideoViewHolder> {

    constructor() : super() {}

    constructor(videoData: MutableList<VideoData>, clickListener: BaseTouchAdapter.OnItemClickListener, dragListener: BaseTouchAdapter.OnItemDragListener) : super(videoData, clickListener, dragListener) {}

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener) : super(clickListener) {}

    constructor(dragListener: BaseTouchAdapter.OnItemDragListener) : super(dragListener) {}

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener, dragListener: BaseTouchAdapter.OnItemDragListener) : super(clickListener, dragListener) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_video_list, parent, false)
        return VideoViewHolder(view)
    }

    companion object {

        val TAG = "VideoItemAdapter"
    }

}
