package com.arman.queuetube.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arman.queuetube.R
import com.arman.queuetube.listeners.OnShowPopupMenuListener
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.viewholders.VideoItemViewHolder
import com.arman.queuetube.model.viewholders.YouTubeThumbnailViewHolder

open class VideoItemAdapter : YouTubeThumbnailAdapter {

    var onShowPopupMenuListener: OnShowPopupMenuListener? = null
    protected var itemResId: Int = R.layout.item_video

    constructor() : super()

    constructor(onShowPopupMenuListener: OnShowPopupMenuListener?) : this(null, onShowPopupMenuListener)

    constructor(itemResId: Int, onShowPopupMenuListener: OnShowPopupMenuListener?) : super() {
        this.itemResId = itemResId
        this.onShowPopupMenuListener = onShowPopupMenuListener
    }

    constructor(itemResId: Int) : super() {
        this.itemResId = itemResId
    }

    constructor(itemResId: Int, dragListener: OnItemDragListener) : super(dragListener) {
        this.itemResId = itemResId
    }

    constructor(itemResId: Int, clickListener: OnItemClickListener) : super(clickListener) {
        this.itemResId = itemResId
    }

    constructor(clickListener: OnItemClickListener?, onShowPopupMenuListener: OnShowPopupMenuListener?) : this(clickListener, null, onShowPopupMenuListener)

    constructor(clickListener: OnItemClickListener?, dragListener: OnItemDragListener?, onShowPopupMenuListener: OnShowPopupMenuListener?) : super(clickListener, dragListener) {
        this.onShowPopupMenuListener = onShowPopupMenuListener
    }

    constructor(videoData: ArrayList<VideoData>, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) : super(videoData, clickListener, dragListener)

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener?) : super(clickListener)

    constructor(dragListener: BaseTouchAdapter.OnItemDragListener?) : super(dragListener)

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) : super(clickListener, dragListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(this.itemResId, parent, false)
        return VideoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: YouTubeThumbnailViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val optionsButton = holder.itemView.findViewById<View>(R.id.playlist_item_options_button)
        optionsButton.setOnClickListener { view -> onShowPopupMenuListener?.onShowPopupMenu(holder, view) }
    }

    override fun onItemDragged(fromIndex: Int, toIndex: Int, dragFinished: Boolean): Boolean {
        if (!dragFinished) {
            return super.onItemDragged(fromIndex, toIndex, false)
        }
        return false
    }

    companion object {

        const val TAG = "VideoItemAdapter"

    }

}
