package com.arman.queuetube.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arman.queuetube.R
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.viewholders.BaseViewHolder
import com.arman.queuetube.model.viewholders.PlaylistItemViewHolder
import com.arman.queuetube.model.viewholders.VideoViewHolder
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class PlaylistItemAdapter : VideoItemAdapter {

    var playlistName: String? = null
        private set
    private var onShowPopupMenuListener: OnShowPopupMenuListener? = null

    constructor(playlistName: String, onShowPopupMenuListener: OnShowPopupMenuListener) : this(playlistName) {
        this.onShowPopupMenuListener = onShowPopupMenuListener
    }

    constructor(playlistName: String, dragListener: BaseTouchAdapter.OnItemDragListener) : super(dragListener) {
        this.playlistName = playlistName
    }

    constructor(playlistName: String, clickListener: BaseTouchAdapter.OnItemClickListener) : super(clickListener) {
        this.playlistName = playlistName
    }

    constructor() : super() {}

    constructor(playlistName: String) : super() {
        this.playlistName = playlistName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_playlist, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val optionsButton = holder.itemView.findViewById<View>(R.id.playlist_item_options_button)
        optionsButton.setOnClickListener { view -> onShowPopupMenuListener?.onShowPopupMenu(holder, view) }
    }

    override fun onItemDragged(fromIndex: Int, toIndex: Int, dragFinished: Boolean): Boolean {
        if (dragFinished) {
            GsonPlaylistHelper.reorder(this.playlistName, fromIndex, toIndex)
            return true
        } else {
            return super.onItemDragged(fromIndex, toIndex, false)
        }
    }

    interface OnShowPopupMenuListener {

        fun onShowPopupMenu(holder: BaseViewHolder<VideoData>, anchorView: View)

    }

}
