package com.arman.queuetube.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arman.queuetube.R
import com.arman.queuetube.activities.PlaylistActivity
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.model.adapters.PlaylistsAdapter
import com.arman.queuetube.modules.playlists.LoadPlaylistsTask

class LibraryFragment : Fragment() {

    private var playlistsView: RecyclerView? = null
    var playlistsAdapter: PlaylistsAdapter? = null
        private set
    private var refreshLayout: SwipeRefreshLayout? = null
    private var loadPlaylistsTask: LoadPlaylistsTask? = null

    fun loadPlaylists() {
        val status = this.loadPlaylistsTask!!.status
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                this.loadPlaylistsTask = LoadPlaylistsTask(this)
            }
            this.loadPlaylistsTask!!.execute()
        }
    }

    fun finishRefresh() {
        this.refreshLayout!!.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        this.loadPlaylists()
    }

    private fun loadActivity(name: String) {
        val intent = Intent(activity, PlaylistActivity::class.java)
        intent.putExtra(Constants.Fragment.Argument.PLAYLIST_NAME, name)
        this.startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.refreshLayout = view.findViewById<View>(R.id.playlists_view_swipe_container) as SwipeRefreshLayout
        this.playlistsView = this.refreshLayout!!.findViewById<View>(R.id.playlists_view) as RecyclerView
        this.playlistsView!!.setHasFixedSize(true)

        this.playlistsAdapter = PlaylistsAdapter(BaseTouchAdapter.OnItemClickListener { viewHolder ->
            val name = this@LibraryFragment.playlistsAdapter!!.get(viewHolder.adapterPosition)
            loadActivity(name)
        })

        this.playlistsView!!.adapter = this.playlistsAdapter

        this.loadPlaylistsTask = LoadPlaylistsTask(this)

        val createPlaylistButton = view.findViewById<View>(R.id.playlists_create_playlist_button) as Button
        createPlaylistButton.setOnClickListener {
            val dialog = CreatePlaylistFragment()
            dialog.show(fragmentManager!!, "create_playlist_dialog")
        }

        this.refreshLayout!!.setOnRefreshListener { this@LibraryFragment.loadPlaylists() }

    }

}
