package com.arman.queuetube.modules.playlists.json

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.arman.queuetube.config.Constants
import com.arman.queuetube.listeners.OnSaveFinishedListener
import com.arman.queuetube.listeners.OnTaskFinishedListener
import com.arman.queuetube.model.Video
import com.arman.queuetube.modules.BaseTask
import com.arman.queuetube.modules.InBackground
import com.google.gson.*
import java.io.*
import kotlin.math.min

@SuppressLint("StaticFieldLeak")
object GsonPlaylistHelper {

    private var context: Context? = null
    private val onSaveFinishedListeners: MutableList<OnSaveFinishedListener> = ArrayList()
    private var savePlaylistsTask: BaseTask<String, Unit>? = null

    private var gson: Gson? = null

    val favorites: JsonArray?
        get() = getPlaylist(Constants.Json.Playlist.FAVORITES)

    val history: JsonArray?
        get() = getPlaylist(Constants.Json.Playlist.HISTORY)

    val playlists: JsonArray?
        get() = getPlaylists(read())

    val userPlaylists: JsonArray
        get() {
            val root = read()
            val playlists = root.get(Constants.Json.Key.PLAYLISTS).asJsonArray
            val userPlaylists = JsonArray()
            for (playlist in playlists) {
                val obj = playlist.asJsonObject
                val name = obj.getAsJsonPrimitive(Constants.Json.Key.NAME).asString
                if (name != Constants.Json.Playlist.FAVORITES && name != Constants.Json.Playlist.HISTORY) {
                    userPlaylists.add(playlist)
                }
            }
            return userPlaylists
        }

    fun addOnSaveFinishedListener(onSaveFinishedListener: OnSaveFinishedListener) {
        this.onSaveFinishedListeners.add(onSaveFinishedListener)
    }

    fun removeOnSaveFinishedListener(onSaveFinishedListener: OnSaveFinishedListener) {
        this.onSaveFinishedListeners.remove(onSaveFinishedListener)
    }

    private fun doExecuteSave(vararg strings: String): Unit {
        try {
            doWrite(strings[0])
        } catch (e: IOException) {
        }
    }

    private fun executeSave(string: String = "", doInBackground: InBackground<String, Unit> = ::doExecuteSave) {
        if (savePlaylistsTask == null) {
            savePlaylistsTask = BaseTask(doInBackground)
            savePlaylistsTask?.onTaskFinishedListener = OnTaskFinishedListener {
                onSaveFinishedListeners.forEach { it.onSaveFinished() }
            }
        }
        val status = savePlaylistsTask!!.status
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                savePlaylistsTask = BaseTask(doInBackground)
                savePlaylistsTask?.onTaskFinishedListener = OnTaskFinishedListener {
                    onSaveFinishedListeners.forEach { it.onSaveFinished() }
                }
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

    fun create(context: Context) {
        GsonPlaylistHelper.context = context
        gson = GsonBuilder().setPrettyPrinting().create()
        val fileExists = doesFileExist()
        if (!fileExists) {
            executeSave {
                val root = JsonObject()
                val playlists = JsonArray()
                playlists.add(newPlaylist(Constants.Json.Playlist.HISTORY))
                playlists.add(newPlaylist(Constants.Json.Playlist.FAVORITES))

                root.add(Constants.Json.Key.PLAYLISTS, playlists)
                doWrite(root.toString())
            }
        }
    }

    fun isFavorited(video: Video): Boolean {
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

    fun playlistNames(array: JsonArray): MutableList<String> {
        val strings = java.util.ArrayList<String>()
        for (i in 0 until array.size()) {
            val playlist = array.get(i).asJsonObject
            strings.add(playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).asString)
        }
        return strings
    }

    fun asPlaylist(array: JsonArray): MutableList<Video> {
        val videos = ArrayList<Video>()
        for (i in 0 until array.size()) {
            val video = array.get(i).asJsonObject
            val videoData = Video(video)
            videos.add(videoData)
        }
        return videos
    }

    fun asPlaylist(array: JsonArray, size: Int): MutableList<Video> {
        val videos = ArrayList<Video>()
        for (i in 0 until min(size, array.size())) {
            val video = array.get(i).asJsonObject
            val videoData = Video(video)
            videos.add(videoData)
        }
        return videos
    }

    private fun newPlaylist(name: String): JsonObject {
        val obj = JsonObject()
        obj.addProperty(Constants.Json.Key.NAME, name)
        obj.add(Constants.Json.Key.PLAYLIST, JsonArray())
        return obj
    }

    fun editName(fromName: String, toName: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)!!
        for (i in 0 until playlists.size()) {
            val obj = playlists.get(i).asJsonObject
            if (obj.getAsJsonPrimitive(Constants.Json.Key.NAME).asString == fromName) {
                executeSave {
                    obj.addProperty(Constants.Json.Key.NAME, toName)
                    doWrite(root.toString())
                }
                return true
            }
        }
        return false
    }

    private fun createVideo(video: Video): JsonObject {
        val obj = JsonObject()
        obj.addProperty(Constants.VideoData.ID, video.id)
        obj.addProperty(Constants.VideoData.TITLE, video.title)
        obj.addProperty(Constants.VideoData.CHANNEL_TITLE, video.channelTitle)
        obj.addProperty(Constants.VideoData.PUBLISHED_AT, video.publishedAt)
        obj.addProperty(Constants.VideoData.LIVE_BROADCAST_CONTENT, video.liveBroadcastContent)
        return obj
    }

    private fun getPlaylists(root: JsonObject): JsonArray? {
        return root.get(Constants.Json.Key.PLAYLISTS).asJsonArray
    }

    fun getPlaylist(name: String): JsonArray? {
        val root = read()
        return getPlaylist(root, name)
    }

    private fun getPlaylist(root: JsonObject, name: String): JsonArray? {
        val playlists = getPlaylists(root)!!
        for (i in 0 until playlists.size()) {
            val obj = playlists.get(i).asJsonObject
            if (obj.getAsJsonPrimitive(Constants.Json.Key.NAME).asString == name) {
                return obj.get(Constants.Json.Key.PLAYLIST).asJsonArray
            }
        }
        return null
    }

    fun writeNewIfNotFound(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)

        for (i in 0 until playlists!!.size()) {
            val playlist = playlists.get(i).asJsonObject
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).asString == name) {
                return false
            }
        }

        executeSave {
            val obj = newPlaylist(name)
            playlists.add(obj)
            doWrite(root.toString())
        }

        return true
    }

    private fun insert(array: JsonArray, pos: Int, obj: JsonObject) {
        array.add(JsonObject())
        for (i in array.size() - 1 downTo pos + 1) {
            array.set(i, array.get(i - 1))
        }
        array.set(pos, obj)
    }

    fun reorder(name: String, fromIndex: Int, toIndex: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            executeSave {
                val video = playlist.remove(fromIndex).asJsonObject
                insert(playlist, toIndex, video)
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun reorder(name: String, video: Video, toIndex: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (i in 0 until playlist.size()) {
                val obj = playlist.get(i).asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    executeSave {
                        playlist.remove(i)
                        insert(playlist, toIndex, obj)
                        doWrite(root.toString())
                    }
                    return true
                }
            }
        }
        return false
    }

    fun removeFrom(name: String, video: Video): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (i in 0 until playlist.size()) {
                val obj = playlist.get(i).asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    executeSave {
                        playlist.remove(i)
                        doWrite(root.toString())
                    }
                    return true
                }
            }
        }
        return false
    }

    fun writeToNew(name: String, video: Video): Boolean {
        val root = read()
        val playlists = getPlaylists(root)

        var playlist = getPlaylist(root, name)
        if (playlist == null) {
            val obj = newPlaylist(name)
            playlists!!.add(obj)
            playlist = obj.getAsJsonArray(Constants.Json.Key.PLAYLIST)
        }
        if (playlist != null) {
            executeSave {
                playlist.add(createVideo(video))
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun writeToIfNotFound(name: String, video: Video): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (jsonElement in playlist) {
                val obj = jsonElement.asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    return false
                }
            }
            executeSave {
                playlist.add(createVideo(video))
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun writeToIfNotFound(name: String, video: Video, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            for (jsonElement in playlist) {
                val obj = jsonElement.asJsonObject
                if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                    return false
                }
            }
            executeSave {
                insert(playlist, index, createVideo(video))
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun writeToOrReorder(name: String, video: Video, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            executeSave {
                for (i in 0 until playlist.size()) {
                    val obj = playlist.get(i).asJsonObject
                    if (obj.get(Constants.Json.Key.ID).asString == video.id) {
                        playlist.remove(i)
                        insert(playlist, index, obj)
                        doWrite(root.toString())
                        return@executeSave
                    }
                }
                insert(playlist, index, createVideo(video))
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun writeTo(name: String, video: Video, index: Int): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            executeSave {
                insert(playlist, index, createVideo(video))
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun writeTo(name: String, video: Video): Boolean {
        val root = read()
        val playlist = getPlaylist(root, name)
        if (playlist != null) {
            executeSave {
                playlist.add(createVideo(video))
                doWrite(root.toString())
            }
            return true
        }
        return false
    }

    fun read(): JsonObject {
        return try {
            doRead()
        } catch (e: IOException) {
            JsonObject()
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

    fun remove(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)
        for (i in 0 until playlists!!.size()) {
            val playlist = playlists.get(i).asJsonObject
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).asString == name) {
                executeSave {
                    playlists.remove(i)
                    doWrite(root.toString())
                }
                return true
            }
        }
        return false
    }

    fun clear(name: String): Boolean {
        val root = read()
        val playlists = getPlaylists(root)
        for (i in 0 until playlists!!.size()) {
            val playlist = playlists.get(i).asJsonObject
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).asString == name) {
                executeSave {
                    playlists.remove(i)
                    insert(playlists, i, newPlaylist(name))
                    doWrite(root.toString())
                }
                return true
            }
        }
        return false
    }

}
