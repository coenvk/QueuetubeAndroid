package com.arman.queuetube.model.adapters

import com.arman.queuetube.listeners.OnShowPopupMenuListener
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class PlaylistItemAdapter : VideoItemAdapter {

    var playlistName: String = ""
        private set

    constructor(itemResId: Int, playlistName: String) : super(itemResId) {
        this.playlistName = playlistName
    }

    constructor(playlistName: String, onShowPopupMenuListener: OnShowPopupMenuListener) : this(playlistName) {
        this.onShowPopupMenuListener = onShowPopupMenuListener
    }

    constructor(playlistName: String, dragListener: OnItemDragListener) : super(dragListener) {
        this.playlistName = playlistName
    }

    constructor(playlistName: String, clickListener: OnItemClickListener) : super(clickListener) {
        this.playlistName = playlistName
    }

    constructor() : super()

    constructor(playlistName: String) : super() {
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