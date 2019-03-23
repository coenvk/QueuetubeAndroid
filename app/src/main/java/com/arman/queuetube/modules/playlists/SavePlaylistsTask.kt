package com.arman.queuetube.modules.playlists

import android.os.AsyncTask

import com.arman.queuetube.activities.MainActivity
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

import java.io.IOException

class SavePlaylistsTask(private val activity: MainActivity) : AsyncTask<String, Int, Void>() {

    override fun doInBackground(vararg strings: String): Void? {
        try {
            GsonPlaylistHelper.doWrite(strings[0])
        } catch (e: IOException) {

        }

        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(aVoid: Void) {
        super.onPostExecute(aVoid)
        this.activity.refreshPlaylists()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        super.onProgressUpdate(*values)
    }

    override fun onCancelled(aVoid: Void) {
        super.onCancelled(aVoid)
    }

    override fun onCancelled() {
        super.onCancelled()
    }

}
