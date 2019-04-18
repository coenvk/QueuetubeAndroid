package com.arman.queuetube.model

import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import com.arman.queuetube.config.Constants
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video
import com.google.gson.JsonObject

class Video : Parcelable {

    companion object {

        const val PREFIX_THUMBNAIL_URL = "https://i.ytimg.com/vi/"
        const val POSTFIX_THUMBNAIL_URL = ".jpg"

        @JvmField
        val CREATOR = object : Parcelable.Creator<com.arman.queuetube.model.Video> {
            override fun createFromParcel(parcel: Parcel): com.arman.queuetube.model.Video {
                return Video(parcel)
            }

            override fun newArray(size: Int): Array<com.arman.queuetube.model.Video?> {
                return arrayOfNulls(size)
            }
        }

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
    var publishedAt: String? = null
    var channelTitle: String? = null
    var liveBroadcastContent: String? = null

    fun isLive(): Boolean {
        return this.liveBroadcastContent == "live"
    }

    var isFavorite: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        publishedAt = parcel.readString()
        channelTitle = parcel.readString()
        liveBroadcastContent = parcel.readString()
        isFavorite = parcel.readByte() != 0.toByte()
    }

    constructor()

    constructor(id: String) {
        this.id = id
    }

    constructor(title: String, id: String, publishedAt: String, channelTitle: String) {
        this.setTo(title, id, publishedAt, channelTitle)
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

    fun setTo(title: String, id: String, publishedAt: String, channelTitle: String, liveBroadcastContent: String = "none", isFavorite: Boolean = false) {
        this.title = title
        this.id = id
        this.publishedAt = publishedAt
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

    fun setTo(video: Video) {
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

    fun setTo(videoData: com.arman.queuetube.model.Video) {
        this.title = videoData.title
        this.id = videoData.id
        this.channelTitle = videoData.channelTitle
        this.publishedAt = videoData.publishedAt
        this.liveBroadcastContent = videoData.liveBroadcastContent
        this.isFavorite = videoData.isFavorite
    }

    override fun equals(o: Any?): Boolean {
        if (o is com.arman.queuetube.model.Video) {
            val other = o as com.arman.queuetube.model.Video?
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
        result = 31 * result + (publishedAt?.hashCode() ?: 0)
        result = 31 * result + (channelTitle?.hashCode() ?: 0)
        result = 31 * result + (liveBroadcastContent?.hashCode() ?: 0)
        result = 31 * result + isFavorite.hashCode()
        return result
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

}
