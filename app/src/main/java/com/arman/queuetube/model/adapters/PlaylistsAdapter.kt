package com.arman.queuetube.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.arman.queuetube.R
import com.arman.queuetube.model.viewholders.PlaylistsViewHolder

class PlaylistsAdapter : BaseTouchAdapter<String, PlaylistsViewHolder> {

    constructor() : super() {}

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener) : super(clickListener) {}

    constructor(dragListener: BaseTouchAdapter.OnItemDragListener) : super(dragListener) {}

    constructor(clickListener: BaseTouchAdapter.OnItemClickListener, dragListener: BaseTouchAdapter.OnItemDragListener) : super(clickListener, dragListener) {}

    constructor(items: MutableList<String>, clickListener: BaseTouchAdapter.OnItemClickListener, dragListener: BaseTouchAdapter.OnItemDragListener) : super(items, clickListener, dragListener) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_playlists, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val textView = holder.itemView.findViewById<View>(R.id.playlists_item_title) as TextView
        textView.text = this.items[position]
    }

    companion object {

        val TAG = "PlaylistsAdapter"
    }

}
