package com.arman.queuetube.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.PlaylistItemAdapter
import com.arman.queuetube.model.adapters.VideoItemAdapter
import com.arman.queuetube.model.viewholders.BaseViewHolder
import com.arman.queuetube.modules.playlists.LoadPlaylistTask
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.util.VideoSharer
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlaylistFragment : Fragment(), PlaylistItemAdapter.OnShowPopupMenuListener {

    var playlistName: String? = null
        private set
    private var isClickable: Boolean = false
    private var isSortable: Boolean = false

    private var headerTitleView: TextView? = null
    private var headerSubtitleView: TextView? = null

    private var refreshLayout: SwipeRefreshLayout? = null
    private var playlistView: RecyclerView? = null
    var playlistAdapter: VideoItemAdapter? = null
        private set

    private var loadPlaylistTask: LoadPlaylistTask? = null

    private var playAllButton: FloatingActionButton? = null

    private var itemTouchHelper: ItemTouchHelper? = null

    private var onPlayItemsListener: OnPlayItemsListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist1, container, false) as ViewGroup
    }

    fun setOnPlayItemsListener(onPlayItemsListener: OnPlayItemsListener) {
        this.onPlayItemsListener = onPlayItemsListener
    }

    fun setTitle(title: String) {
        this.playlistName = title
        this.headerTitleView!!.text = this.playlistName
    }

    fun setSubtitle(subtitle: String) {
        this.headerSubtitleView!!.text = subtitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.playlistName = arguments.getString(Constants.Fragment.Argument.PLAYLIST_NAME)
            this.isClickable = arguments.getBoolean(Constants.Fragment.Argument.IS_CLICKABLE, true)
            this.isSortable = arguments.getBoolean(Constants.Fragment.Argument.IS_SORTABLE, true)
        }
    }

    fun loadPlaylist() {
        if (this.playlistName != null) {
            val status = this.loadPlaylistTask!!.status
            if (status != AsyncTask.Status.RUNNING) {
                if (status == AsyncTask.Status.FINISHED) {
                    this.loadPlaylistTask = LoadPlaylistTask(this)
                }
                this.loadPlaylistTask!!.execute(this.playlistName)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        this.loadPlaylist()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.headerTitleView = view.findViewById<View>(R.id.playlist_header_title) as TextView
        this.headerTitleView!!.text = this.playlistName

        this.headerSubtitleView = view.findViewById<View>(R.id.playlist_header_subtitle) as TextView

        this.refreshLayout = view.findViewById<View>(R.id.playlist_view_swipe_container) as SwipeRefreshLayout
        this.playlistView = this.refreshLayout!!.findViewById<View>(R.id.playlist_view) as RecyclerView
        this.playlistView!!.setHasFixedSize(true)

        this.playlistAdapter = PlaylistItemAdapter(this.playlistName, this)
        this.playlistView!!.adapter = this.playlistAdapter

        this.loadPlaylistTask = LoadPlaylistTask(this)

        this.playAllButton = view.findViewById<View>(R.id.playlist_play_all_button) as FloatingActionButton
        this.playAllButton!!.setOnClickListener {
            if (this@PlaylistFragment.onPlayItemsListener != null) {
                this@PlaylistFragment.onPlayItemsListener!!.onPlayAll(this@PlaylistFragment.playlistAdapter!!.all)
            }
        }

        val clearButton = view.findViewById<View>(R.id.playlist_clear_button) as ImageView
        clearButton.setOnClickListener { GsonPlaylistHelper.clear(this@PlaylistFragment.playlistName) }

        if (this.isClickable) {
            val callback = VideoItemTouchHelper.Callback(this.playlistAdapter)
            this.itemTouchHelper = ItemTouchHelper(callback)
            this.itemTouchHelper!!.attachToRecyclerView(this.playlistView)
        }

        this.refreshLayout!!.setOnRefreshListener { this@PlaylistFragment.loadPlaylist() }

    }

    fun finishRefresh() {
        this.refreshLayout!!.isRefreshing = false
        val videoCount = this.playlistAdapter!!.itemCount
        val subtitle = "$videoCount videos"
        this.headerSubtitleView!!.text = subtitle
        if (videoCount <= 0) {
            this.playAllButton!!.visibility = View.GONE
        } else {
            this.playAllButton!!.visibility = View.VISIBLE
        }
    }

    override fun onShowPopupMenu(holder: BaseViewHolder<VideoData>, anchorView: View) {
        val context = anchorView.context
        val optionsPopup = PopupMenu(context, anchorView)
        optionsPopup.menuInflater.inflate(R.menu.playlist_item_popup_menu, optionsPopup.menu)
        optionsPopup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            val item = holder.item
            when (menuItem.itemId) {
                R.id.playlist_item_option_add_to_queue -> onPlayItemsListener!!.onPlay(item)
                R.id.playlist_item_option_remove -> {
                    playlistAdapter!!.remove(item)
                    GsonPlaylistHelper.removeFrom(playlistName, item)
                }
                R.id.playlist_item_option_share -> VideoSharer.share(context, item)
                else -> return@OnMenuItemClickListener false
            }
            true
        })
        optionsPopup.show()
    }

    interface OnPlayItemsListener {

        fun onPlayAll(videos: Collection<VideoData>)

        fun onPlay(video: VideoData)

    }

    companion object {

        val TAG = "PlaylistFragment"
    }

}
