package com.arman.queuetube.modules.playlists

import android.os.AsyncTask
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.LibraryFragment
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class LoadPlaylistsTask(private val libraryFragment: LibraryFragment) : AsyncTask<Void, Int, MutableList<String>>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(strings: MutableList<String>) {
        super.onPostExecute(strings)
        strings.remove(Constants.Json.Playlist.FAVORITES)
        strings.remove(Constants.Json.Playlist.HISTORY)
        this.libraryFragment.playlistsAdapter!!.setAll(strings)
        this.libraryFragment.finishRefresh()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        super.onProgressUpdate(*values)
    }

    override fun onCancelled(strings: MutableList<String>) {
        super.onCancelled(strings)
    }

    override fun onCancelled() {
        super.onCancelled()
    }

    private fun getNames(array: JsonArray): MutableList<String> {
        val strings = ArrayList<String>()
        for (i in 0 until array.size()) {
            val playlist = array.get(i).asJsonObject
            strings.add(playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).toString())
        }
        return strings
    }

    private fun getNames(array: JSONArray): MutableList<String> {
        val strings = ArrayList<String>()
        try {
            for (i in 0 until array.length()) {
                val playlist = array.getJSONObject(i)
                strings.add(playlist.getString(Constants.Json.Key.NAME))
            }
        } catch (e: JSONException) {

        }

        return strings
    }

    override fun doInBackground(vararg voids: Void): MutableList<String> {
        val playlists = GsonPlaylistHelper.playlists
        return getNames(playlists!!)
    }

}
