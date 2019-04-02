package com.arman.queuetube.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.arman.queuetube.R
import com.arman.queuetube.model.viewholders.PlaylistsViewHolder

class PlaylistsAdapter : BaseTouchAdapter<String, PlaylistsViewHolder> {

    constructor() : super()

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener) : super(clickListener)

    constructor(dragListener: BaseTouchAdapter.OnItemDragListener) : super(dragListener)

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener, dragListener: BaseTouchAdapter.OnItemDragListener) : super(clickListener, dragListener)

    constructor(items: MutableList<String>, clickListener: BaseTouchAdapter.OnItemClickListener, dragListener: BaseTouchAdapter.OnItemDragListener) : super(items, clickListener, dragListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_playlists, parent, false)
        return PlaylistsViewHolder(view)
    }

    companion object {

        const val TAG = "PlaylistsAdapter"

    }

}
