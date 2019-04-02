package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
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
    var onSaveFinishedListener: OnSaveFinishedListener = OnSaveFinishedListener { onSaveFinished() }

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

    open fun onAddToQueue(item: VideoData) {
        onPlayItemsListener?.onPlay(item)
    }

    open fun onPlayNext(item: VideoData) {
        onPlayItemsListener?.onPlayNext(item)
    }

    open fun onPlayNow(item: VideoData) {
        onPlayItemsListener?.onPlayNow(item)
    }

    open fun onRemoveFromPlaylist(item: VideoData) {

    }

    open fun onShare(item: VideoData) {
        VideoSharer.share(context, item)
    }

    final override fun onShowPopupMenu(holder: BaseViewHolder<VideoData>, anchorView: View) {
        val optionsPopup = PopupMenu(context, anchorView)
        optionsPopup.menuInflater.inflate(this.popupMenuResId, optionsPopup.menu)

        optionsPopup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            val item = holder.item
            item?.let {
                when (menuItem.itemId) {
                    R.id.list_item_option_add_to_queue -> onAddToQueue(item)
                    R.id.list_item_option_remove -> onRemoveFromPlaylist(item)
                    R.id.list_item_option_play_next -> onPlayNext(item)
                    R.id.list_item_option_play_now -> onPlayNow(item)
                    R.id.list_item_option_share -> onShare(item)
                    else -> return@OnMenuItemClickListener false
                }
                return@OnMenuItemClickListener true
            }
            false
        })
        optionsPopup.show()
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder) {
        if (holder is VideoItemViewHolder) {
            onPlayItemsListener?.onPlay(holder.item!!)
        }
    }

}