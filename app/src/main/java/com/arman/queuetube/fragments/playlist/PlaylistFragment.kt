package com.arman.queuetube.fragments.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.ItemTouchHelper
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.RefreshVideoListFragment
import com.arman.queuetube.fragments.dialogs.EditPlaylistNameFragment
import com.arman.queuetube.listeners.OnDialogDismissListener
import com.arman.queuetube.listeners.OnSaveFinishedListener
import com.arman.queuetube.model.Video
import com.arman.queuetube.model.adapters.PlaylistItemAdapter
import com.arman.queuetube.model.viewholders.BaseViewHolder
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper
import kotlinx.android.synthetic.main.fragment_playlist.*

class PlaylistFragment : RefreshVideoListFragment() {

    var playlistName: String = ""
        private set

    private var isEditable: Boolean = true

    private val onSaveFinishedListener: OnSaveFinishedListener = OnSaveFinishedListener { load() }

    @MenuRes
    override val popupMenuResId: Int = R.menu.popup_menu_playlist_item

    override fun onDestroy() {
        super.onDestroy()
        GsonPlaylistHelper.removeOnSaveFinishedListener(onSaveFinishedListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    fun setTitle(title: String) {
        this.playlistName = title
        playlist_header_title.text = this.playlistName
    }

    fun setSubtitle(subtitle: String) {
        playlist_header_subtitle.text = subtitle
    }

    fun onEdit() {
        val bundle = Bundle()
        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, this.playlistName)
        val dialog = EditPlaylistNameFragment()
        dialog.arguments = bundle
        dialog.onDialogDismissListener = OnDialogDismissListener {
            this.setTitle(it.getString(Constants.Fragment.Argument.PLAYLIST_NAME, this.playlistName))
        }
        dialog.show(fragmentManager!!, "edit_playlist_dialog")
    }

    fun onClear() {
        GsonPlaylistHelper.clear(this@PlaylistFragment.playlistName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.playlistName = arguments.getString(Constants.Fragment.Argument.PLAYLIST_NAME, "")
            this.isEditable = arguments.getBoolean(Constants.Fragment.Argument.IS_EDITABLE, true)
        }
        GsonPlaylistHelper.addOnSaveFinishedListener(onSaveFinishedListener)
    }

    override fun doInBackground(vararg params: String): MutableList<Video> {
        val playlist = GsonPlaylistHelper.getPlaylist(this.playlistName)
        return GsonPlaylistHelper.asPlaylist(playlist!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.listAdapter = PlaylistItemAdapter(this.playlistName, this, this, this)
        list_view.adapter = this.listAdapter

        if (this.isDraggable) {
            this.itemTouchHelper?.attachToRecyclerView(null)
            val callback = VideoItemTouchHelper.Callback(this.listAdapter!!)
            this.itemTouchHelper = ItemTouchHelper(callback)
            this.itemTouchHelper!!.attachToRecyclerView(list_view)
        }

        playlist_header_title.text = this.playlistName

        val shuffleButton = list_shuffle_button
        if (this.isShufflable) {
            shuffleButton.setOnClickListener { onShuffle() }
        } else {
            shuffleButton.visibility = View.GONE
        }

        val clearButton = playlist_clear_button
        clearButton.setOnClickListener { onClear() }

        val editButton = playlist_edit_button
        if (this.isEditable) {
            editButton.setOnClickListener { onEdit() }
        } else {
            editButton.visibility = View.GONE
        }
    }

    override fun finishLoad() {
        super.finishLoad()
        val videoCount = this.listAdapter!!.itemCount
        this.setSubtitle("$videoCount videos")
    }

    override fun onRemoveFromPlaylist(holder: BaseViewHolder<Video>) {
        val item = holder.item!!
        listAdapter!!.remove(item)
        GsonPlaylistHelper.removeFrom(this@PlaylistFragment.playlistName, item)
    }

    override fun onAddToQueue(holder: BaseViewHolder<Video>) {
        if (onPlayItemsListener != null) {
            super.onAddToQueue(holder)
        } else {
            val intent = Intent(Constants.Action.Play.PLAY_ACTION)
            intent.putParcelableArrayListExtra(Constants.Fragment.Argument.VIDEO_LIST, arrayListOf(holder.item))
            context!!.sendBroadcast(intent)
        }
    }

    override fun onPlayNext(holder: BaseViewHolder<Video>) {
        if (onPlayItemsListener != null) {
            super.onPlayNext(holder)
        } else {
            val intent = Intent(Constants.Action.Play.PLAY_NEXT_ACTION)
            intent.putParcelableArrayListExtra(Constants.Fragment.Argument.VIDEO_LIST, arrayListOf(holder.item))
            context!!.sendBroadcast(intent)
        }
    }

    override fun onPlayNow(holder: BaseViewHolder<Video>) {
        if (onPlayItemsListener != null) {
            super.onPlayNow(holder)
        } else {
            val intent = Intent(Constants.Action.Play.PLAY_NOW_ACTION)
            intent.putParcelableArrayListExtra(Constants.Fragment.Argument.VIDEO_LIST, arrayListOf(holder.item))
            context!!.sendBroadcast(intent)
        }
    }

    override fun onPlayAll() {
        if (onPlayItemsListener != null) {
            super.onPlayAll()
        } else {
            val intent = Intent(Constants.Action.Play.PLAY_ALL_ACTION)
            intent.putParcelableArrayListExtra(Constants.Fragment.Argument.VIDEO_LIST, this.listAdapter!!.all)
            context!!.sendBroadcast(intent)
        }
    }

    override fun onShuffle() {
        if (onPlayItemsListener != null) {
            super.onShuffle()
        } else {
            val intent = Intent(Constants.Action.Play.SHUFFLE_ACTION)
            intent.putParcelableArrayListExtra(Constants.Fragment.Argument.VIDEO_LIST, this.listAdapter!!.all)
            context!!.sendBroadcast(intent)
        }
    }

    companion object {

        const val TAG = "PlaylistFragment"

    }

}
