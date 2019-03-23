package com.arman.queuetube.modules.playlists.json

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.arman.queuetube.activities.MainActivity
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.playlists.SavePlaylistsTask
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*

@SuppressLint("StaticFieldLeak")
object JSONPlaylistHelper {

    private var context: Context? = null
    private var savePlaylistsTask: SavePlaylistsTask? = null

    val favorites: JSONArray?
        get() = getPlaylist(read(), Constants.Json.Playlist.FAVORITES)

    val history: JSONArray?
        get() = getPlaylist(read(), Constants.Json.Playlist.HISTORY)

    val playlists: JSONArray
        get() = getPlaylists(read())

    val userPlaylists: JSONArray
        get() {
            val root = read()
            val playlists = getPlaylists(root)
            val userPlaylists = JSONArray()
            try {
                for (i in 0 until playlists.length()) {
                    val obj = playlists.getJSONObject(i)
                    val name = obj.getString(Constants.Json.Key.NAME)
                    if (name != Constants.Json.Playlist.FAVORITES && name != Constants.Json.Playlist.HISTORY) {
                        userPlaylists.put(obj)
                    }
                }
            } catch (e: JSONException) {

            }

            return userPlaylists
        }

    private fun executeSave(string: String) {
        val status = savePlaylistsTask!!.status
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                savePlaylistsTask = SavePlaylistsTask(context as MainActivity?)
            }
            savePlaylistsTask!!.execute(string)
        }
    }

    @Throws(IOException::class)
    fun doWrite(string: String) {
        val fos = context!!.openFileOutput(Constants.Json.STORAGE_FILE_NAME, Context.MODE_PRIVATE)
        val bw = BufferedWriter(OutputStreamWriter(fos))
        bw.write(string)
        bw.flush()
        bw.close()
    }

    @Throws(IOException::class)
    private fun doRead(): String {
        val fis = context!!.openFileInput(Constants.Json.STORAGE_FILE_NAME)
        val br = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()
        var line: String?
        do {
            line = br.readLine()
            if (line == null) break
            sb.append(line)
        } while (true)
        br.close()
        return sb.toString()
    }

    fun onCreate(context: Context) {
        JSONPlaylistHelper.context = context
        savePlaylistsTask = SavePlaylistsTask(context as MainActivity)
        val fileExists = doesFileExist()
        try {
            if (!fileExists) {
                val root = JSONObject()
                val playlists = JSONArray()
                playlists.put(newPlaylist(Constants.Json.Playlist.HISTORY))
                playlists.put(newPlaylist(Constants.Json.Playlist.FAVORITES))

                root.put(Constants.Json.Key.PLAYLISTS, playlists)
                executeSave(root.toString())
            }
        } catch (e: JSONException) {

        }

    }

    fun isFavorited(video: VideoData): Boolean {
        val favorites = favorites
        try {
            for (i in 0 until favorites!!.length()) {
                val obj = favorites.getJSONObject(i)
                val id = obj.getString(Constants.Json.Key.ID)
                if (id == video.id) {
                    return true
                }
            }
        } catch (e: JSONException) {

        }

        return false
    }

    private fun newPlaylist(name: String?): JSONObject {
        val obj = JSONObject()
        try {
            obj.put(Constants.Json.Key.NAME, name)
            obj.put(Constants.Json.Key.PLAYLIST, JSONArray())
        } catch (e: JSONException) {

        }

        return obj
    }

    private fun createVideo(video: VideoData): JSONObject {
        val obj = JSONObject()
        try {
            obj.put(Constants.Json.Key.ID, video.id)
            obj.put(Constants.VideoData.TITLE, video.title)
            obj.put(Constants.VideoData.THUMBNAIL_URL, video.thumbnailUrl)
            obj.put(Constants.VideoData.CHANNEL, video.channel)
            obj.put(Constants.VideoData.PUBLISHED_ON, video.publishedOn)
        } catch (e: JSONException) {

        }

        return obj
    }

    private fun getPlaylists(root: JSONObject): JSONArray {
        try {
            return root.getJSONArray(Constants.Json.Key.PLAYLISTS)
        } catch (e: JSONException) {
            return JSONArray()
        }

    }

    fun getPlaylist(name: String): JSONArray? {
        val root = read()
        return getPlaylist(root, name)
    }

    private fun getPlaylist(root: JSONObject, name: String): JSONArray? {
        val playlists = getPlaylists(root)
        try {
            for (i in 0 until playlists.length()) {
                val obj = playlists.getJSONObject(i)
                if (obj.getString(Constants.Json.Key.NAME) == name) {
                    return obj.getJSONArray(Constants.Json.Key.PLAYLIST)
                }
            }
        } catch (e: JSONException) {

        }

        return null
    }

    fun writeNewIfNotFound(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)

        try {
            for (i in 0 until playlists.length()) {
                val playlist = playlists.getJSONObject(i)
                if (playlist.getString(Constants.Json.Key.NAME) == name) {
                    return false
                }
            }
        } catch (e: JSONException) {
            return false
        }

        val obj = newPlaylist(name)

        playlists.put(obj)
        executeSave(root.toString())
        return true
    }

    fun writeNew(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)

        val obj = newPlaylist(name)

        playlists.put(obj)
        executeSave(root.toString())
        return true
    }

    private fun insert(array: JSONArray, pos: Int, obj: JSONObject): Boolean {
        try {
            for (i in array.length() downTo pos + 1) {
                array.put(i, array.get(i - 1))
            }
            array.put(pos, obj)
        } catch (e: JSONException) {
            return false
        }

        return true
    }

    fun reorder(name: String, fromIndex: Int, toIndex: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            val video = playlist.remove(fromIndex) as JSONObject
            if (!insert(playlist, toIndex, video)) {
                return false
            }
            executeSave(root.toString())
            return true
        }
        return false
    }

    fun reorder(name: String, video: VideoData, toIndex: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            try {
                for (i in 0 until playlist.length()) {
                    val obj = playlist.getJSONObject(i)
                    if (obj.getString(Constants.Json.Key.ID) == video.id) {
                        playlist.remove(i)
                        if (!insert(playlist, toIndex, obj)) {
                            return false
                        }
                        executeSave(root.toString())
                        return true
                    }
                }
            } catch (e: JSONException) {
                return false
            }

        }
        return false
    }

    fun removeFrom(name: String, video: VideoData): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            try {
                for (i in 0 until playlist.length()) {
                    val obj = playlist.getJSONObject(i)
                    if (obj.getString(Constants.Json.Key.ID) == video.id) {
                        playlist.remove(i)
                        executeSave(root.toString())
                        return true
                    }
                }
            } catch (e: JSONException) {
                return false
            }

        }
        return false
    }

    fun writeToNew(name: String, video: VideoData): Boolean {
        val root = read()
        val playlists = getPlaylists(root)

        var playlist = getPlaylist(root, name)
        if (playlist == null) {
            try {
                val obj = newPlaylist(name)
                playlists.put(obj)
                playlist = obj.getJSONArray(Constants.Json.Key.PLAYLIST)
            } catch (e: JSONException) {
                return false
            }

        }
        if (playlist != null) {
            playlist.put(createVideo(video))
            executeSave(root.toString())
            return true
        }
        return false
    }

    fun writeToIfNotFound(name: String, video: VideoData): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            try {
                for (i in 0 until playlist.length()) {
                    val obj = playlist.getJSONObject(i)
                    if (obj.getString(Constants.Json.Key.ID) == video.id) {
                        return false
                    }
                }
            } catch (e: JSONException) {
                return false
            }

            playlist.put(createVideo(video))
            executeSave(root.toString())
            return true
        }
        return false
    }

    fun writeToIfNotFound(name: String, video: VideoData, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            try {
                for (i in 0 until playlist.length()) {
                    val obj = playlist.getJSONObject(i)
                    if (obj.getString(Constants.Json.Key.ID) == video.id) {
                        return false
                    }
                }
            } catch (e: JSONException) {
                return false
            }

            if (!insert(playlist, index, createVideo(video))) {
                return false
            }
            executeSave(root.toString())
            return true
        }
        return false
    }

    fun writeToOrReorder(name: String, video: VideoData, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            try {
                for (i in 0 until playlist.length()) {
                    val obj = playlist.getJSONObject(i)
                    if (obj.getString(Constants.Json.Key.ID) == video.id) {
                        playlist.remove(i)
                        if (!insert(playlist, index, obj)) {
                            return false
                        }
                        executeSave(root.toString())
                        return true
                    }
                }
            } catch (e: JSONException) {
                return false
            }

        }
        return false
    }

    fun writeTo(name: String, video: VideoData, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            if (!insert(playlist, index, createVideo(video))) {
                return false
            }
            executeSave(root.toString())
            return true
        }
        return false
    }

    fun writeTo(name: String, video: VideoData): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            playlist.put(createVideo(video))
            executeSave(root.toString())
            return true
        }
        return false
    }

    private fun printInParts(string: String) {
        val max = 1000
        for (i in 0..string.length / max) {
            val start = i * max
            var end = (i + 1) * max
            end = if (end > string.length) string.length else end
            println(string.substring(start, end))
        }
    }

    fun read(): JSONObject {
        try {
            return JSONObject(doRead())
        } catch (e: JSONException) {
            return JSONObject()
        } catch (e: IOException) {
            return JSONObject()
        }

    }

    private fun write(obj: JSONObject): Boolean {
        val json = obj.toString()
        executeSave(json)
        return true
    }

    private fun doesFileExist(): Boolean {
        val path = context!!.filesDir.absolutePath + "/" + Constants.Json.STORAGE_FILE_NAME
        val file = File(path)
        return file.exists()
    }

    fun remove(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)
        try {
            for (i in 0 until playlists.length()) {
                val playlist = playlists.getJSONObject(i)
                if (playlist.getString(Constants.Json.Key.NAME) == name) {
                    playlists.remove(i)
                    executeSave(root.toString())
                    return true
                }
            }
        } catch (e: JSONException) {

        }

        return false
    }

    fun clear(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)
        try {
            for (i in 0 until playlists.length()) {
                val playlist = playlists.getJSONObject(i)
                if (playlist.getString(Constants.Json.Key.NAME) == name) {
                    playlists.remove(i)
                    playlists.put(newPlaylist(name))
                    executeSave(root.toString())
                    return true
                }
            }
        } catch (e: JSONException) {

        }

        return false
    }

}
