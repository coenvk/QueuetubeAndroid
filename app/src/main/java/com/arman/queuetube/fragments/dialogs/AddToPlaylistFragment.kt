package com.arman.queuetube.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.Video
import com.arman.queuetube.modules.BaseTask
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.google.gson.JsonArray
import java.util.*
import java.util.concurrent.ExecutionException

class AddToPlaylistFragment : DialogFragment() {

    private var selectedItems: MutableList<Int>? = null
    var video: Video? = null

    private val task = BaseTask<Unit, Pair<Array<CharSequence?>, BooleanArray>> {
        return@BaseTask loadPlaylists(GsonPlaylistHelper.userPlaylists)
    }

    private fun loadPlaylists(array: JsonArray): Pair<Array<CharSequence?>, BooleanArray> {
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
        return Pair(strings, checked)
    }

    private fun createErrorDialog(): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle("Error")
                .setMessage("An error occurred while trying to retrieve your playlists")
                .setPositiveButton("OK") { _, _ -> }
        return builder.create()
    }

    private fun createDialog(items: Array<CharSequence?>, checkedItems: BooleanArray): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle("Save video to...")
                .setPositiveButton("OK") { _, _ ->
                    if (video != null) {
                        for (j in items.indices) {
                            val name = items[j].toString()
                            if (this@AddToPlaylistFragment.selectedItems!!.contains(j)) {
                                GsonPlaylistHelper.writeTo(name, video!!)
                            } else {
                                GsonPlaylistHelper.removeFrom(name, video!!)
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .setMultiChoiceItems(items, checkedItems) { _, i, b ->
                    if (b) {
                        this@AddToPlaylistFragment.selectedItems!!.add(i)
                    } else if (this@AddToPlaylistFragment.selectedItems!!.contains(i)) {
                        this@AddToPlaylistFragment.selectedItems!!.remove(Integer.valueOf(i))
                    }
                }
        if (items.isEmpty()) {
            builder.setMessage("There are no playlists yet!")
        }
        return builder.create()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        this.selectedItems = ArrayList()
        return try {
            val (left, right) = this.task.execute().get()
            createDialog(left, right)
        } catch (e: ExecutionException) {
            createErrorDialog()
        } catch (e: InterruptedException) {
            createErrorDialog()
        }
    }

}
