package com.arman.queuetube.fragments.main

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.activities.PlaylistActivity
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.AsyncVideoListFragment
import com.arman.queuetube.fragments.dialogs.CreatePlaylistFragment
import com.arman.queuetube.listeners.OnSaveFinishedListener
import com.arman.queuetube.listeners.OnTaskFinishedListener
import com.arman.queuetube.model.Video
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.model.adapters.PlaylistsAdapter
import com.arman.queuetube.model.adapters.VideoItemAdapter
import com.arman.queuetube.modules.BaseTask
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import kotlinx.android.synthetic.main.button_create_playlist.*
import kotlinx.android.synthetic.main.fragment_library.*

class LibraryFragment : AsyncVideoListFragment() {

    var playlistsAdapter: PlaylistsAdapter? = null
        private set

    private var loadPlaylistsTask: BaseTask<Unit, MutableList<String>>? = null

    private val onSaveFinishedListener: OnSaveFinishedListener = OnSaveFinishedListener { load() }

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

    override fun doInBackground(vararg params: String): MutableList<Video> {
        return GsonPlaylistHelper.asPlaylist(GsonPlaylistHelper.history!!, Constants.History.MAX_SIZE_SMALL)
    }

    override fun load(vararg params: String) {
        super.load(*params)
        this.loadPlaylists()
    }

    private fun loadPlaylistActivity(name: String) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GsonPlaylistHelper.addOnSaveFinishedListener(onSaveFinishedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        GsonPlaylistHelper.removeOnSaveFinishedListener(onSaveFinishedListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.listAdapter = VideoItemAdapter(R.layout.item_video_card_small, this, this)
        list_view.adapter = this.listAdapter

        val moreButton = list_more_button
        moreButton.setOnClickListener { loadPlaylistActivity(Constants.Json.Playlist.HISTORY) }

        playlists_view.setHasFixedSize(true)

        this.playlistsAdapter = PlaylistsAdapter(object : BaseTouchAdapter.OnItemClickListener {
            override fun onItemClick(holder: RecyclerView.ViewHolder) {
                val name = this@LibraryFragment.playlistsAdapter!![holder.adapterPosition]
                loadPlaylistActivity(name)
            }
        })

        playlists_view.adapter = this.playlistsAdapter

        val createPlaylistButton = playlists_create_playlist_button
        createPlaylistButton.setOnClickListener {
            val dialog = CreatePlaylistFragment()
            dialog.show(fragmentManager!!, "create_playlist_dialog")
        }
    }

    companion object {

        const val TAG = "LibraryFragment"

    }

}
