package com.arman.queuetube.modules.playlists.json

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.arman.queuetube.activities.MainActivity
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.playlists.SavePlaylistsTask
import com.google.gson.*
import java.io.*

@SuppressLint("StaticFieldLeak")
object GsonPlaylistHelper {

    private val PRIM_FAVORITES = JsonPrimitive(Constants.Json.Playlist.FAVORITES)
    private val PRIM_HISTORY = JsonPrimitive(Constants.Json.Playlist.HISTORY)

    private var context: Context? = null
    private var savePlaylistsTask: SavePlaylistsTask? = null

    private var gson: Gson? = null

    val favorites: JsonArray?
        get() = getPlaylist(read(), GsonPlaylistHelper.PRIM_FAVORITES)

    val history: JsonArray?
        get() = getPlaylist(read(), GsonPlaylistHelper.PRIM_HISTORY)

    val playlists: JsonArray?
        get() = getPlaylists(read()!!)

    val userPlaylists: JsonArray
        get() {
            val root = read()
            val playlists = root!!.get(Constants.Json.Key.PLAYLISTS).asJsonArray
            val userPlaylists = JsonArray()
            for (playlist in playlists) {
                val obj = playlist.asJsonObject
                val name = obj.getAsJsonPrimitive(Constants.Json.Key.NAME)
                if (name != PRIM_FAVORITES && name != PRIM_HISTORY) {
                    userPlaylists.add(playlist)
                }
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
    private fun doRead(): JsonObject {
        val fis = context!!.openFileInput(Constants.Json.STORAGE_FILE_NAME)
        val br = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()
        var line: String?
        do {
            line = br.readLine()
            if (line == null) break
            sb.append(line)
        } while (true)
        val root = gson!!.fromJson(sb.toString(), JsonElement::class.java).asJsonObject
        br.close()
        return root
    }

    fun onCreate(context: Context) {
        GsonPlaylistHelper.context = context
        savePlaylistsTask = SavePlaylistsTask(context as MainActivity)
        gson = GsonBuilder().setPrettyPrinting().create()
        val fileExists = doesFileExist()
        if (!fileExists) {
            val root = JsonObject()
            val playlists = JsonArray()
            playlists.add(newPlaylist(GsonPlaylistHelper.PRIM_HISTORY))
            playlists.add(newPlaylist(GsonPlaylistHelper.PRIM_FAVORITES))

            root.add(Constants.Json.Key.PLAYLISTS, playlists)
            executeSave(root.toString())
        }
    }

    fun isFavorited(video: VideoData): Boolean {
        val favorites = favorites
        for (i in 0 until favorites!!.size()) {
            val obj = favorites.get(i).asJsonObject
            val id = obj.get(Constants.Json.Key.ID).asString
            if (id == video.id) {
                return true
            }
        }
        return false
    }

    private fun newPlaylist(name: JsonPrimitive?): JsonObject {
        val obj = JsonObject()
        obj.add(Constants.Json.Key.NAME, name)
        obj.add(Constants.Json.Key.PLAYLIST, JsonArray())
        return obj
    }

    private fun newPlaylist(name: String): JsonObject {
        val obj = JsonObject()
        obj.addProperty(Constants.Json.Key.NAME, name)
        obj.add(Constants.Json.Key.PLAYLIST, JsonArray())
        return obj
    }

    private fun createVideo(video: VideoData): JsonObject {
        return gson!!.toJsonTree(video).asJsonObject
    }

    private fun getPlaylists(root: JsonObject): JsonArray? {
        return root.get(Constants.Json.Key.PLAYLISTS).asJsonArray
    }

    fun getPlaylist(name: JsonPrimitive): JsonArray? {
        val root = read()
        return getPlaylist(root, name)
    }

    fun getPlaylist(name: String): JsonArray? {
        return getPlaylist(JsonPrimitive(name))
    }

    private fun getPlaylist(root: JsonObject?, name: JsonPrimitive): JsonArray? {
        val playlists = getPlaylists(root!!)!!
        for (i in 0 until playlists.size()) {
            val obj = playlists.get(i).asJsonObject
            if (obj.getAsJsonPrimitive(Constants.Json.Key.NAME) == name) {
                return obj.get(Constants.Json.Key.PLAYLIST).asJsonArray
            }
        }
        return null
    }

    private fun getPlaylist(root: JsonObject, name: String): JsonArray? {
        return getPlaylist(root, JsonPrimitive(name))
    }

    fun writeNewIfNotFound(name: JsonPrimitive): Boolean {
        val root = read()
        val playlists = getPlaylists(root!!)

        for (i in 0 until playlists!!.size()) {
            val playlist = playlists.get(i).asJsonObject
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME) == name) {
                return false
            }
        }

        val obj = newPlaylist(name)

        playlists.add(obj)
        executeSave(root.toString())
        return true
    }

    fun writeNewIfNotFound(name: String): Boolean {
        return writeNewIfNotFound(JsonPrimitive(name))
    }

    fun writeNew(name: JsonPrimitive): Boolean {
        val root = read()!!
        val playlists = getPlaylists(root)!!

        val obj = newPlaylist(name)

        playlists.add(obj)
        executeSave(root.toString())
        return true
    }

    fun writeNew(name: String): Boolean {
        return writeNew(JsonPrimitive(name))
    }

    private fun insert(array: JsonArray, pos: Int, obj: JsonObject): Boolean {
        array.add(JsonObject())
        for (i in array.size() - 1 downTo pos + 1) {
            array.set(i, array.get(i - 1))
        }
        array.set(pos, obj)
        return true
    }

    fun reorder(name: JsonPrimitive, fromIndex: Int, toIndex: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            val video = playlist.remove(fromIndex).asJsonObject
            if (!insert(playlist, toIndex, video)) {
                return false
            }
            executeSave(root!!.toString())
            return true
        }
        return false
    }

    fun reorder(name: String, fromIndex: Int, toIndex: Int): Boolean {
        return reorder(JsonPrimitive(name), fromIndex, toIndex)
    }

    fun reorder(name: JsonPrimitive, video: VideoData, toIndex: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (i in 0 until playlist.size()) {
                val obj = playlist.get(i).asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    playlist.remove(i)
                    if (!insert(playlist, toIndex, obj)) {
                        return false
                    }
                    executeSave(root!!.toString())
                    return true
                }
            }
        }
        return false
    }

    fun reorder(name: String, video: VideoData, toIndex: Int): Boolean {
        return reorder(JsonPrimitive(name), video, toIndex)
    }

    fun removeFrom(name: JsonPrimitive, video: VideoData): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (i in 0 until playlist.size()) {
                val obj = playlist.get(i).asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    playlist.remove(i)
                    executeSave(root!!.toString())
                    return true
                }
            }
        }
        return false
    }

    fun removeFrom(name: String, video: VideoData): Boolean {
        return removeFrom(JsonPrimitive(name), video)
    }

    fun writeToNew(name: JsonPrimitive, video: VideoData): Boolean {
        val root = read()
        val playlists = getPlaylists(root!!)

        var playlist = getPlaylist(root, name)
        if (playlist == null) {
            val obj = newPlaylist(name)
            playlists!!.add(obj)
            playlist = obj.getAsJsonArray(Constants.Json.Key.PLAYLIST)
        }
        if (playlist != null) {
            playlist.add(createVideo(video))
            executeSave(root.toString())
            return true
        }
        return false
    }

    fun writeToNew(name: String, video: VideoData): Boolean {
        return writeToNew(JsonPrimitive(name), video)
    }

    fun writeToIfNotFound(name: JsonPrimitive, video: VideoData): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (jsonElement in playlist) {
                val obj = jsonElement.asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    return false
                }
            }
            playlist.add(createVideo(video))
            executeSave(root!!.toString())
            return true
        }
        return false
    }

    fun writeToIfNotFound(name: String, video: VideoData): Boolean {
        return writeToIfNotFound(JsonPrimitive(name), video)
    }

    fun writeToIfNotFound(name: JsonPrimitive, video: VideoData, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (jsonElement in playlist) {
                val obj = jsonElement.asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    return false
                }
            }
            if (!insert(playlist, index, createVideo(video))) {
                return false
            }
            executeSave(root!!.toString())
            return true
        }
        return false
    }

    fun writeToIfNotFound(name: String, video: VideoData, index: Int): Boolean {
        return writeToIfNotFound(JsonPrimitive(name), video, index)
    }

    fun writeToOrReorder(name: JsonPrimitive, video: VideoData, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (i in 0 until playlist.size()) {
                val obj = playlist.get(i).asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    playlist.remove(i)
                    if (!insert(playlist, index, obj)) {
                        return false
                    }
                    executeSave(root!!.toString())
                    return true
                }
            }
        }
        return false
    }

    fun writeToOrReorder(name: String, video: VideoData, index: Int): Boolean {
        return writeToOrReorder(JsonPrimitive(name), video, index)
    }

    fun writeTo(name: JsonPrimitive, video: VideoData, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            if (!insert(playlist, index, createVideo(video))) {
                return false
            }
            executeSave(root!!.toString())
            return true
        }
        return false
    }

    fun writeTo(name: String, video: VideoData, index: Int): Boolean {
        return writeTo(JsonPrimitive(name), video, index)
    }

    fun writeTo(name: JsonPrimitive, video: VideoData): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            playlist.add(createVideo(video))
            executeSave(root!!.toString())
            return true
        }
        return false
    }

    fun writeTo(name: String, video: VideoData): Boolean {
        return writeTo(JsonPrimitive(name), video)
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

    fun read(): JsonObject? {
        try {
            return doRead()
        } catch (e: IOException) {
            System.err.println(e.message)
            return JsonObject()
        }

    }

    private fun write(obj: JsonObject): Boolean {
        val json = obj.toString()
        executeSave(json)
        return true
    }

    private fun doesFileExist(): Boolean {
        val path = context!!.filesDir.absolutePath + "/" + Constants.Json.STORAGE_FILE_NAME
        val file = File(path)
        return file.exists()
    }

    fun remove(name: JsonPrimitive): Boolean {
        val root = read()
        val playlists = getPlaylists(root!!)
        for (i in 0 until playlists!!.size()) {
            val playlist = playlists.get(i).asJsonObject
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME) == name) {
                playlists.remove(i)
                executeSave(root.toString())
                return true
            }
        }
        return false
    }

    fun remove(name: String): Boolean {
        return remove(JsonPrimitive(name))
    }

    fun clear(name: JsonPrimitive): Boolean {
        val root = read()
        val playlists = getPlaylists(root!!)
        for (i in 0 until playlists!!.size()) {
            val playlist = playlists.get(i).asJsonObject
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME) == name) {
                playlists.remove(i)
                playlists.add(newPlaylist(name))
                executeSave(root.toString())
                return true
            }
        }
        return false
    }

    fun clear(name: String): Boolean {
        return clear(JsonPrimitive(name))
    }

}
