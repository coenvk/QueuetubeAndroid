package com.arman.queuetube.fragments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.util.Tuple
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import java.util.concurrent.ExecutionException

class AddToPlaylistFragment : DialogFragment() {

    private var selectedItems: MutableList<Int>? = null
    private var video: VideoData? = null

    @SuppressLint("StaticFieldLeak")
    private val loadPlaylistsTask = object : AsyncTask<Unit, Int, Tuple<Array<CharSequence?>, BooleanArray>>() {
        override fun doInBackground(vararg voids: Unit): Tuple<Array<CharSequence?>, BooleanArray> {
            val playlists = GsonPlaylistHelper.userPlaylists
            return loadPlaylists(playlists)
        }
    }

    private fun loadPlaylists(array: JsonArray): Tuple<Array<CharSequence?>, BooleanArray> {
        val strings = arrayOfNulls<CharSequence>(array.size())
        val checked = BooleanArray(strings.size)
        for (i in strings.indices) {
            val obj = array.get(i).asJsonObject
            strings[i] = obj.getAsJsonPrimitive(Constants.Json.Key.NAME).asString
            val playlist = obj.getAsJsonArray(Constants.Json.Key.PLAYLIST)
            for (j in 0 until playlist.size()) {
                val vid = playlist.get(j).asJsonObject
                if (vid.get(Constants.Json.Key.ID).asString == video!!.id) {
                    checked[i] = true
                    break
                }
            }
        }
        return Tuple(strings, checked)
    }

    private fun loadPlaylists(array: JSONArray): Tuple<Array<CharSequence?>, BooleanArray> {
        val strings = arrayOfNulls<CharSequence>(array.length())
        val checked = BooleanArray(strings.size)
        try {
            for (i in strings.indices) {
                val obj = array.getJSONObject(i)
                strings[i] = obj.getString(Constants.Json.Key.NAME)
                val playlist = obj.getJSONArray(Constants.Json.Key.PLAYLIST)
                for (j in 0 until playlist.length()) {
                    val vid = playlist.getJSONObject(j)
                    if (vid.getString(Constants.Json.Key.ID) == video!!.id) {
                        checked[i] = true
                        break
                    }
                }
            }
        } catch (e: JSONException) {

        }

        return Tuple<Array<CharSequence?>, BooleanArray>(strings, checked)
    }

    fun setVideo(video: VideoData) {
        this.video = video
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        this.selectedItems = ArrayList()
        val tuple: Tuple<Array<CharSequence?>, BooleanArray>
        try {
            tuple = this.loadPlaylistsTask.execute().get()
        } catch (e: ExecutionException) {
            val builder = AlertDialog.Builder(activity)
            builder
                    .setTitle("Error")
                    .setMessage("An error occurred while trying to retrieve your playlists")
                    .setPositiveButton("OK") { _, _ -> }
            return builder.create()
        } catch (e: InterruptedException) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Error").setMessage("An error occurred while trying to retrieve your playlists").setPositiveButton("OK") { _, _ -> }
            return builder.create()
        }

        val playlists = tuple.left
        val checked = tuple.right

        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle("Save video to...")
                .setPositiveButton("OK") { _, _ ->
                    if (video != null) {
                        for (j in playlists.indices) {
                            val name = playlists[j].toString()
                            if (this@AddToPlaylistFragment.selectedItems!!.contains(j)) {
                                GsonPlaylistHelper.writeTo(name, video!!)
                            } else {
                                GsonPlaylistHelper.removeFrom(name, video!!)
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .setMultiChoiceItems(playlists, checked) { _, i, b ->
                    if (b) {
                        this@AddToPlaylistFragment.selectedItems!!.add(i)
                    } else if (this@AddToPlaylistFragment.selectedItems!!.contains(i)) {
                        this@AddToPlaylistFragment.selectedItems!!.remove(Integer.valueOf(i))
                    }
                }
        return builder.create()
    }

    interface OnDialogDismissListener {

        fun onDialogAccept(playlists: Array<CharSequence>, selectedItems: Collection<Int>)

        fun onDialogCancel()

    }

}
