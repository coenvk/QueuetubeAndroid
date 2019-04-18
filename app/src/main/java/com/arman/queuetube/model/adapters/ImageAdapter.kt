package com.arman.queuetube.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.arman.queuetube.R
import com.arman.queuetube.model.Video
import com.arman.queuetube.model.viewholders.ImageViewHolder

open class ImageAdapter : BaseTouchAdapter<Video, ImageViewHolder> {

    constructor() : super()

    constructor(videoData: ArrayList<Video>, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) : super(videoData, clickListener, dragListener)

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener?) : super(clickListener)

    constructor(dragListener: BaseTouchAdapter.OnItemDragListener?) : super(dragListener)

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) : super(clickListener, dragListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_video, parent, false)
        return ImageViewHolder(view)
    }

    companion object {

        const val TAG = "ImageAdapter"

    }

}