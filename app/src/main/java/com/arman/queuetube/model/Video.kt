package com.arman.queuetube.model

import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import com.arman.queuetube.config.Constants
import com.google.api.services.youtube.model.SearchResult
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Video() : Parcelable {

    companion object {

        const val PREFIX_THUMBNAIL_URL = "https://i.ytimg.com/vi/"
        const val POSTFIX_THUMBNAIL_URL = ".jpg"

        @JvmField
        val CREATOR = object : Parcelable.Creator<Video> {
            override fun createFromParcel(parcel: Parcel): Video {
                return Video(parcel)
            }

            override fun newArray(size: Int): Array<Video?> {
                return arrayOfNulls(size)
            }
        }

    }

    private inner class Id {

        @SerializedName("videoId")
        @Expose
        lateinit var videoId: String
    }

    private inner class Snippet {

        @SerializedName("title")
        @Expose
        lateinit var title: String

        @SerializedName("publishedAt")
        @Expose
        lateinit var publishedAt: String

        @SerializedName("channelTitle")
        @Expose
        lateinit var channelTitle: String

        @SerializedName("liveBroadcastContent")
        @Expose
        lateinit var liveBroadcastContent: String

    }

    @SerializedName("id")
    @Expose
    private lateinit var _id: Id

    @SerializedName("snippet")
    @Expose
    private lateinit var _snippet: Snippet

    var id: String
        get() = _id.videoId
        set(value) {
            _id.videoId = value
        }

    var title: String
        get() = _snippet.title
        set(value) {
            _snippet.title = Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT).toString()
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
            return VideoData.PREFIX_THUMBNAIL_URL + this.id + "/mqdefault" + VideoData.POSTFIX_THUMBNAIL_URL
        }
    val highThumbnailUrl: String
        get() {
            return VideoData.PREFIX_THUMBNAIL_URL + this.id + "/hqdefault" + VideoData.POSTFIX_THUMBNAIL_URL
        }

    var publishedAt: String
        get() = _snippet.publishedAt
        set(value) {
            _snippet.publishedAt = value
        }

    var channelTitle: String
        get() = _snippet.channelTitle
        set(value) {
            _snippet.channelTitle = value
        }

    var liveBroadcastContent: String
        get() = _snippet.liveBroadcastContent
        set(value) {
            _snippet.liveBroadcastContent = value
        }

    fun isLive(): Boolean {
        return this.liveBroadcastContent == "live"
    }

    var isFavorite: Boolean = false

    constructor(id: String) : this() {
        this.id = id
    }

    constructor(id: String, title: String, publishedAt: String, channelTitle: String) : this() {
        this.setTo(id, title, publishedAt, channelTitle)
    }

    constructor(result: SearchResult) : this() {
        this.setTo(result)
    }

    constructor(video: Video) : this() {
        this.setTo(video)
    }

    constructor(video: com.google.api.services.youtube.model.Video) : this() {
        this.setTo(video)
    }

    constructor(obj: JsonObject) : this() {
        this.setTo(obj)
    }

    fun setTo(id: String, title: String, publishedOn: String, channelTitle: String, liveBroadcastContent: String = "none", isFavorite: Boolean = false) {
        this.title = title
        this.id = id
        this.publishedAt = publishedOn
        this.channelTitle = channelTitle
        this.liveBroadcastContent = liveBroadcastContent
        this.isFavorite = isFavorite
    }

    fun setTo(result: SearchResult) {
        this.title = result.snippet.title
        this.id = result.id.videoId
        this.publishedAt = result.snippet.publishedAt.toString()
        this.channelTitle = result.snippet.channelTitle
        this.liveBroadcastContent = result.snippet.liveBroadcastContent
    }

    fun setTo(video: com.google.api.services.youtube.model.Video) {
        this.title = video.snippet.title
        this.id = video.id
        this.publishedAt = video.snippet.publishedAt.toString()
        this.channelTitle = video.snippet.channelTitle
        this.liveBroadcastContent = video.snippet.liveBroadcastContent
    }

    fun setTo(obj: JsonObject) {
        this.title = obj.get(Constants.VideoData.TITLE).asString
        this.id = obj.get(Constants.VideoData.ID).asString
        this.publishedAt = obj.get(Constants.VideoData.PUBLISHED_AT).asString
        this.channelTitle = obj.get(Constants.VideoData.CHANNEL_TITLE).asString
        this.liveBroadcastContent = obj.get(Constants.VideoData.LIVE_BROADCAST_CONTENT).asString
    }

    fun setTo(video: Video) {
        this.title = video.title
        this.id = video.id
        this.channelTitle = video.channelTitle
        this.publishedAt = video.publishedAt
        this.liveBroadcastContent = video.liveBroadcastContent
        this.isFavorite = video.isFavorite
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()!!
        title = parcel.readString()!!
        publishedAt = parcel.readString()!!
        channelTitle = parcel.readString()!!
        liveBroadcastContent = parcel.readString()!!
        isFavorite = parcel.readByte() != 0.toByte()
    }

    override fun toString(): String {
        return "[$id]: $title"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(publishedAt)
        parcel.writeString(channelTitle)
        parcel.writeString(liveBroadcastContent)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(o: Any?): Boolean {
        if (o is VideoData) {
            val other = o as VideoData?
            return other?.id == this.id
        }
        return false
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + channelTitle.hashCode()
        result = 31 * result + liveBroadcastContent.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }

}