package com.arman.queuetube.fragments.main

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.activities.PlaylistActivity
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.VideoItemFragment
import com.arman.queuetube.fragments.dialogs.CreatePlaylistFragment
import com.arman.queuetube.listeners.OnTaskFinishedListener
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.model.adapters.PlaylistItemAdapter
import com.arman.queuetube.model.adapters.PlaylistsAdapter
import com.arman.queuetube.modules.BaseTask
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class LibraryFragment : VideoItemFragment() {

    private var historyView: RecyclerView? = null
    var historyAdapter: PlaylistItemAdapter? = null
        private set

    private var playlistsView: RecyclerView? = null
    var playlistsAdapter: PlaylistsAdapter? = null
        private set

    private var loadPlaylistsTask: BaseTask<Unit, MutableList<String>>? = null

    private var emptyTextView: TextView? = null

    private fun doLoadPlaylists(vararg aUnit: Unit?): MutableList<String> {
        val playlists = GsonPlaylistHelper.playlists
        return GsonPlaylistHelper.playlistNames(playlists!!)
    }

    private fun loadPlaylists() {
        if (this.loadPlaylistsTask == null) {
            this.loadPlaylistsTask = BaseTask(::doLoadPlaylists)
            this.loadPlaylistsTask?.onTaskFinishedListener = OnTaskFinishedListener {
                it.remove(Constants.Json.Playlist.HISTORY)
                this@LibraryFragment.playlistsAdapter!!.setAll(it)
            }
        }
        val status = this.loadPlaylistsTask!!.status
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                this.loadPlaylistsTask = BaseTask(::doLoadPlaylists)
                this.loadPlaylistsTask?.onTaskFinishedListener = OnTaskFinishedListener {
                    it.remove(Constants.Json.Playlist.HISTORY)
                    this@LibraryFragment.playlistsAdapter!!.setAll(it)
                }
            }
            this.loadPlaylistsTask!!.execute()
        }
    }

    override fun doInBackground(vararg params: String): MutableList<VideoData> {
        val playlist = GsonPlaylistHelper.getPlaylist(Constants.Json.Playlist.HISTORY)
        return GsonPlaylistHelper.asPlaylist(playlist!!)
    }

    override fun onTaskFinished(result: MutableList<VideoData>) {
        this.historyAdapter!!.setAll(result)
        super.onTaskFinished(result)
    }

    override fun onSaveFinished() {
        this.load()
        this.loadPlaylists()
    }

    override fun finishRefresh() {
        if (this.historyAdapter?.isEmpty!!) {
            this.emptyTextView?.visibility = View.VISIBLE
        } else {
            this.emptyTextView?.visibility = View.GONE
        }
    }

    private fun loadActivity(name: String) {
        val intent = Intent(activity, PlaylistActivity::class.java)
        intent.putExtra(Constants.Fragment.Argument.PLAYLIST_NAME, name)
        if (name == Constants.Json.Playlist.FAVORITES) {
            intent.putExtra(Constants.Fragment.Argument.IS_EDITABLE, false)
        } else if (name == Constants.Json.Playlist.HISTORY) {
            intent.putExtra(Constants.Fragment.Argument.IS_EDITABLE, false)
            intent.putExtra(Constants.Fragment.Argument.IS_DRAGGABLE, false)
            intent.putExtra(Constants.Fragment.Argument.IS_SORTABLE, false)
        }
        this.startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.emptyTextView = view.findViewById(R.id.recently_watched_empty_text) as TextView

        this.playlistsView = view.findViewById(R.id.playlists_view) as RecyclerView
        this.playlistsView!!.setHasFixedSize(true)

        this.playlistsAdapter = PlaylistsAdapter(object : BaseTouchAdapter.OnItemClickListener {
            override fun onItemClick(holder: RecyclerView.ViewHolder) {
                val name = this@LibraryFragment.playlistsAdapter!![holder.adapterPosition]
                loadActivity(name)
            }
        })

        this.playlistsView!!.adapter = this.playlistsAdapter

        this.historyView = view.findViewById(R.id.recently_watched_view) as RecyclerView
        this.historyView!!.setHasFixedSize(true)

        this.historyAdapter = PlaylistItemAdapter(R.layout.item_video_card_small, Constants.Json.Playlist.HISTORY)
        this.historyAdapter!!.onItemClickListener = this
        this.historyAdapter!!.onShowPopupMenuListener = this

        this.historyView!!.adapter = this.historyAdapter

        val createPlaylistButton = view.findViewById(R.id.playlists_create_playlist_button) as Button
        createPlaylistButton.setOnClickListener {
            val dialog = CreatePlaylistFragment()
            dialog.show(fragmentManager!!, "create_playlist_dialog")
        }
    }

}
