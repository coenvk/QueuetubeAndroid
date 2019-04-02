package com.arman.queuetube.model

import android.text.Html
import com.arman.queuetube.config.Constants
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject

class VideoData {

    companion object {

        const val PREFIX_THUMBNAIL_URL = "https://i.ytimg.com/vi/"
        const val POSTFIX_THUMBNAIL_URL = ".jpg"

    }

    var title: String? = null
        set(title) {
            field = Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT).toString()
        }
    val thumbnailUrl: String
        get() {
            return PREFIX_THUMBNAIL_URL + this.id + "/default" + POSTFIX_THUMBNAIL_URL
        }
    val defaultThumbnailUrl: String
        get() {
            return this.thumbnailUrl
        }
    val mediumThumbnailUrl: String
        get() {
            return PREFIX_THUMBNAIL_URL + this.id + "/mqdefault" + POSTFIX_THUMBNAIL_URL
        }
    val highThumbnailUrl: String
        get() {
            return PREFIX_THUMBNAIL_URL + this.id + "/hqdefault" + POSTFIX_THUMBNAIL_URL
        }
    var id: String? = null
    var publishedOn: String? = null
    var channel: String? = null

    var isFavorited: Boolean = false

    constructor()

    constructor(id: String) {
        this.id = id
    }

    constructor(title: String, id: String, publishedOn: String, channel: String, description: String) {
        this.title = title
        this.id = id
        this.publishedOn = publishedOn
        this.channel = channel
    }

    constructor(result: SearchResult) {
        this.setTo(result)
    }

    constructor(video: Video) {
        this.setTo(video)
    }

    constructor(obj: JsonObject) {
        this.setTo(obj)
    }

    constructor(obj: JSONObject) {
        this.setTo(obj)
    }

    fun setTo(result: SearchResult) {
        this.title = result.snippet.title
        this.id = result.id.videoId
        this.publishedOn = result.snippet.publishedAt.toString()
        this.channel = result.snippet.channelTitle
    }

    fun setTo(video: Video) {
        this.title = video.snippet.title
        this.id = video.id
        this.publishedOn = video.snippet.publishedAt.toString()
        this.channel = video.snippet.channelTitle
    }

    fun setTo(obj: JsonObject) {
        this.title = obj.get(Constants.VideoData.TITLE).asString
        this.id = obj.get(Constants.VideoData.ID).asString
        this.publishedOn = obj.get(Constants.VideoData.PUBLISHED_ON).asString
        this.channel = obj.get(Constants.VideoData.CHANNEL).asString
    }

    fun setTo(obj: JSONObject) {
        try {
            this.title = obj.getString(Constants.VideoData.TITLE)
            this.id = obj.getString(Constants.VideoData.ID)
            this.publishedOn = obj.getString(Constants.VideoData.PUBLISHED_ON)
            this.channel = obj.getString(Constants.VideoData.CHANNEL)
        } catch (e: JSONException) {
        }
    }

    fun setTo(videoData: VideoData) {
        this.title = videoData.title
        this.id = videoData.id
        this.channel = videoData.channel
        this.publishedOn = videoData.publishedOn
        this.isFavorited = videoData.isFavorited
    }

    override fun equals(o: Any?): Boolean {
        if (o is VideoData) {
            val other = o as VideoData?
            return other?.id == this.id
        }
        return false
    }

    override fun toString(): String {
        return "[" + this.id + "]: " + this.title
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (publishedOn?.hashCode() ?: 0)
        result = 31 * result + (channel?.hashCode() ?: 0)
        result = 31 * result + isFavorited.hashCode()
        return result
    }

}
