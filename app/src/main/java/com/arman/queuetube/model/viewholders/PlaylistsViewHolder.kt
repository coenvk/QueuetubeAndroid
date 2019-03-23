package com.arman.queuetube.model.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class PlaylistsViewHolder(itemView: View) : BaseTouchViewHolder<String>(itemView) {

    private val deleteButton: ImageView
    private val titleView: TextView

    init {
        this.titleView = itemView.findViewById<View>(R.id.playlists_item_title) as TextView
        this.deleteButton = itemView.findViewById<View>(R.id.playlists_item_delete_button) as ImageView
    }

    override fun bind(item: String, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        super.bind(item, clickListener, dragListener)
        if (item == Constants.Json.Playlist.FAVORITES || item == Constants.Json.Playlist.HISTORY) {
            this.deleteButton.visibility = View.GONE
        } else {
            this.deleteButton.setOnClickListener { GsonPlaylistHelper.remove(this@PlaylistsViewHolder.titleView.text.toString()) }
        }
    }

}
