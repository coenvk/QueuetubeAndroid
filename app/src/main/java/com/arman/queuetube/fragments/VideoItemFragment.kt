package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.dialogs.AddToPlaylistFragment
import com.arman.queuetube.listeners.OnPlayItemsListener
import com.arman.queuetube.listeners.OnSaveFinishedListener
import com.arman.queuetube.listeners.OnShowPopupMenuListener
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.model.viewholders.BaseViewHolder
import com.arman.queuetube.model.viewholders.VideoItemViewHolder
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.util.VideoSharer

abstract class VideoItemFragment : AsyncFragment<String, MutableList<VideoData>>(), OnShowPopupMenuListener, BaseTouchAdapter.OnItemClickListener {

    var onPlayItemsListener: OnPlayItemsListener? = null
    private val onSaveFinishedListener: OnSaveFinishedListener = OnSaveFinishedListener { onSaveFinished() }

    @MenuRes
    protected open var popupMenuResId: Int = R.menu.popup_menu_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.params = arrayOf("")
        GsonPlaylistHelper.addOnSaveFinishedListener(this.onSaveFinishedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        GsonPlaylistHelper.removeOnSaveFinishedListener(this.onSaveFinishedListener)
    }

    open fun onSaveFinished() {
        load()
    }

    override fun onStart() {
        super.onStart()
        if (this.loadOnStart) {
            this.onSaveFinished()
        }
    }

    open fun onAddToQueue(holder: BaseViewHolder<VideoData>) {
        onPlayItemsListener?.onPlay(holder.item!!)
    }

    open fun onPlayNext(holder: BaseViewHolder<VideoData>) {
        onPlayItemsListener?.onPlayNext(holder.item!!)
    }

    open fun onPlayNow(holder: BaseViewHolder<VideoData>) {
        onPlayItemsListener?.onPlayNow(holder.item!!)
    }

    open fun onRemoveFromPlaylist(holder: BaseViewHolder<VideoData>) {

    }

    open fun onAddToFavorites(holder: BaseViewHolder<VideoData>) {
        GsonPlaylistHelper.writeToIfNotFound(Constants.Json.Playlist.FAVORITES, holder.item!!)
    }

    open fun onAddToPlaylist(holder: BaseViewHolder<VideoData>) {
        val dialog = AddToPlaylistFragment()
        dialog.setVideo(holder.item!!)
        dialog.show(fragmentManager!!, "add_to_playlist_dialog")
    }

    open fun onShare(holder: BaseViewHolder<VideoData>) {
        VideoSharer.share(context, holder.item!!)
    }

    final override fun onShowPopupMenu(holder: BaseViewHolder<VideoData>, anchorView: View) {
        val optionsPopup = PopupMenu(context, anchorView)
        optionsPopup.menuInflater.inflate(this.popupMenuResId, optionsPopup.menu)

        optionsPopup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_item_option_add_to_queue -> onAddToQueue(holder)
                R.id.list_item_option_remove -> onRemoveFromPlaylist(holder)
                R.id.list_item_option_play_next -> onPlayNext(holder)
                R.id.list_item_option_play_now -> onPlayNow(holder)
                R.id.list_item_option_add_to_favorites -> onAddToFavorites(holder)
                R.id.list_item_option_add_to_playlist -> onAddToPlaylist(holder)
                R.id.list_item_option_share -> onShare(holder)
                else -> return@OnMenuItemClickListener false
            }
            return@OnMenuItemClickListener true
        })
        optionsPopup.show()
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder) {
        if (holder is VideoItemViewHolder) {
            onPlayItemsListener?.onPlay(holder.item!!)
        }
    }

}