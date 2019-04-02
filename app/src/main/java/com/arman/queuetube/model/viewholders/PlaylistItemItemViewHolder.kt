package com.arman.queuetube.model.viewholders

import android.view.View
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.BaseTouchAdapter

class PlaylistItemItemViewHolder(view: View) : VideoItemViewHolder(view) {

    override fun bind(item: VideoData, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        super.bind(item, clickListener, dragListener)
    }

    companion object {

        const val TAG = "PlaylistItemItemViewHolder"

    }

}
