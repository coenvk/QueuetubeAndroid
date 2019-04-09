package com.arman.queuetube.model.adapters

import com.arman.queuetube.listeners.OnShowPopupMenuListener
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class PlaylistItemAdapter : VideoItemAdapter {

    var playlistName: String = ""
        private set

    constructor(itemResId: Int, playlistName: String) : super(itemResId) {
        this.playlistName = playlistName
    }

    constructor(playlistName: String, clickListener: OnItemClickListener? = null, dragListener: OnItemDragListener? = null, onShowPopupMenuListener: OnShowPopupMenuListener? = null) : super(clickListener, dragListener, onShowPopupMenuListener) {
        this.playlistName = playlistName
    }

    override fun onItemDragged(fromIndex: Int, toIndex: Int, dragFinished: Boolean): Boolean {
        return if (dragFinished) {
            GsonPlaylistHelper.reorder(this.playlistName, fromIndex, toIndex)
            true
        } else {
            super.onItemDragged(fromIndex, toIndex, false)
        }
    }

}