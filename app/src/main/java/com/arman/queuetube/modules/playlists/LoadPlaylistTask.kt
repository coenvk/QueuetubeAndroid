package com.arman.queuetube.modules.playlists

import android.os.AsyncTask
import com.arman.queuetube.fragments.PlaylistFragment
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONException

class LoadPlaylistTask(private val playlistFragment: PlaylistFragment) : AsyncTask<String, Int, MutableList<VideoData>>() {

    private fun createPlaylist(array: JsonArray): MutableList<VideoData> {
        val videos = ArrayList<VideoData>()
        for (i in 0 until array.size()) {
            val video = array.get(i).asJsonObject
            val videoData = VideoData(video)
            videos.add(videoData)
        }
        return videos
    }

    private fun createPlaylist(array: JSONArray): MutableList<VideoData> {
        val videos = ArrayList<VideoData>()
        try {
            for (i in 0 until array.length()) {
                val video = array.getJSONObject(i)
                val videoData = VideoData(video)
                videos.add(videoData)
            }
        } catch (e: JSONException) {

        }

        return videos
    }

    override fun doInBackground(vararg strings: String): MutableList<VideoData> {
        val playlist = GsonPlaylistHelper.getPlaylist(strings[0])
        return this.createPlaylist(playlist!!)
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(videoData: MutableList<VideoData>) {
        super.onPostExecute(videoData)
        this.playlistFragment.playlistAdapter!!.setAll(videoData)
        this.playlistFragment.finishRefresh()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        super.onProgressUpdate(*values)
    }

    override fun onCancelled(videoData: MutableList<VideoData>) {
        super.onCancelled(videoData)
    }

    override fun onCancelled() {
        super.onCancelled()
    }

}
