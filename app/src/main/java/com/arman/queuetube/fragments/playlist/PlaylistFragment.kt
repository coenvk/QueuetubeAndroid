package com.arman.queuetube.fragments.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.VideoListFragment
import com.arman.queuetube.fragments.dialogs.EditPlaylistNameFragment
import com.arman.queuetube.listeners.OnDismissDialogListener
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.viewholders.BaseViewHolder
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class PlaylistFragment : VideoListFragment() {

    override var isDraggable: Boolean = true

    var playlistName: String = ""
        private set

    private var isEditable: Boolean = true

    private var emptyTextView: TextView? = null
    private var headerTitleView: TextView? = null
    private var headerSubtitleView: TextView? = null

    override var popupMenuResId: Int = R.menu.popup_menu_playlist_item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false) as ViewGroup
    }

    fun setTitle(title: String) {
        this.playlistName = title
        this.headerTitleView!!.text = this.playlistName
    }

    fun setSubtitle(subtitle: String) {
        this.headerSubtitleView!!.text = subtitle
    }

    fun onEdit() {
        val bundle = Bundle()
        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, this.playlistName)
        val dialog = EditPlaylistNameFragment()
        dialog.arguments = bundle
        dialog.onDismissDialogListener = OnDismissDialogListener {
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
    }

    override fun doInBackground(vararg params: String): MutableList<VideoData> {
        val playlist = GsonPlaylistHelper.getPlaylist(this.playlistName)
        return GsonPlaylistHelper.asPlaylist(playlist!!)
    }

    override fun onTaskFinished(result: MutableList<VideoData>) {
        this.listAdapter!!.setAll(result)
        super.onTaskFinished(result)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.headerTitleView = view.findViewById(R.id.playlist_header_title) as TextView
        this.headerTitleView!!.text = this.playlistName

        this.headerSubtitleView = view.findViewById(R.id.playlist_header_subtitle) as TextView

        this.emptyTextView = view.findViewById(R.id.playlist_empty_text) as TextView

        val shuffleButton = view.findViewById(R.id.playlist_shuffle_button) as ImageView
        if (this.isShufflable) {
            shuffleButton.setOnClickListener { onShuffle() }
        } else {
            shuffleButton.visibility = View.GONE
        }

        val clearButton = view.findViewById(R.id.playlist_clear_button) as ImageView
        clearButton.setOnClickListener { onClear() }

        val editButton = view.findViewById(R.id.playlist_edit_button) as ImageView
        if (this.isEditable) {
            editButton.setOnClickListener { onEdit() }
        } else {
            editButton.visibility = View.GONE
        }
    }

    override fun finishRefresh() {
        super.finishRefresh()
        val videoCount = this.listAdapter!!.itemCount
        val subtitle = "$videoCount videos"
        this.setSubtitle(subtitle)
        if (videoCount <= 0) {
            this.emptyTextView!!.visibility = View.VISIBLE
        } else {
            this.emptyTextView!!.visibility = View.GONE
        }
    }

    override fun onRemoveFromPlaylist(holder: BaseViewHolder<VideoData>) {
        val item = holder.item!!
        listAdapter!!.remove(item)
        GsonPlaylistHelper.removeFrom(this@PlaylistFragment.playlistName, item)
    }

    companion object {

        const val TAG = "PlaylistFragment"

    }

}
