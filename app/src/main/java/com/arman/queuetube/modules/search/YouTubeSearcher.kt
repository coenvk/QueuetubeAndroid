package com.arman.queuetube.modules.search

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.VideoData
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoCategory
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException

class YouTubeSearcher {

    private val youTube: YouTube

    private var searchListQuery: YouTube.Search.List? = null
    private var videosListQuery: YouTube.Videos.List? = null
    private var videoCategoriesListQuery: YouTube.VideoCategories.List? = null

    private var tmpVideoList: MutableList<VideoData>? = null

    init {
        this.tmpVideoList = ArrayList()
        this.youTube = YouTube.Builder(NetHttpTransport(),
                JacksonFactory(), HttpRequestInitializer { }).setApplicationName("Queuetube").build()
    }

    @Throws(IOException::class)
    fun searchList(): YouTube.Search.List {
        return this.searchList(SEARCH_PART)
    }

    @Throws(IOException::class)
    fun searchList(part: String): YouTube.Search.List {
        return this.searchList(part, TYPE_VIDEO)
    }

    @Throws(IOException::class)
    fun searchList(part: String, type: String): YouTube.Search.List {
        return this.searchList(part, type, SEARCH_MAX_RESULTS)
    }

    @Throws(IOException::class)
    fun searchList(part: String, type: String, maxResults: Long): YouTube.Search.List {
        return this.searchList(part, type, maxResults, SEARCH_FIELDS)
    }

    @Throws(IOException::class)
    fun searchList(part: String, type: String, maxResults: Long, fields: String): YouTube.Search.List {
        if (this.searchListQuery == null) {
            this.searchListQuery = this.youTube.search().list(part)
                    .setKey(Constants.Key.API_KEY)
        }
        this.searchListQuery!!
                .setType(type)
                .setMaxResults(maxResults).fields = fields
        return this.searchListQuery!!
    }

    @Throws(IOException::class)
    fun videosList(): YouTube.Videos.List {
        return this.videosList(VIDEOS_PART)
    }

    @Throws(IOException::class)
    fun videosList(maxResults: Long): YouTube.Videos.List {
        return this.videosList(VIDEOS_PART, maxResults)
    }

    @Throws(IOException::class)
    fun videosList(part: String?): YouTube.Videos.List {
        return this.videosList(part, SEARCH_MAX_RESULTS)
    }

    @Throws(IOException::class)
    fun videosList(part: String?, maxResults: Long): YouTube.Videos.List {
        if (this.videosListQuery == null) {
            this.videosListQuery = this.youTube.videos().list(part!!)
                    .setKey(Constants.Key.API_KEY)
        }
        this.videosListQuery!!.maxResults = maxResults
        return this.videosListQuery!!
    }

    @Throws(IOException::class)
    fun videosList(part: String, id: String): YouTube.Videos.List {
        return this.videosList(part, id, VIDEOS_FIELDS)
    }

    @Throws(IOException::class)
    fun videosList(part: String, id: String, fields: String): YouTube.Videos.List {
        return this.videosList(part, id, SEARCH_MAX_RESULTS, fields)
    }

    @Throws(IOException::class)
    fun videosList(part: String, id: String, maxResults: Long): YouTube.Videos.List {
        return this.videosList(part, id, maxResults, VIDEOS_FIELDS)
    }

    @Throws(IOException::class)
    fun videosList(part: String, id: String, maxResults: Long, fields: String): YouTube.Videos.List {
        if (this.videosListQuery == null) {
            this.videosListQuery = this.youTube.videos().list(part)
                    .setKey(Constants.Key.API_KEY)
        }
        this.videosListQuery!!
                .setId(id)
                .setMaxResults(maxResults).fields = fields
        return this.videosListQuery!!
    }

    @Throws(IOException::class)
    fun videosList(part: String, chart: String, videoCategoryId: String, regionCode: String): YouTube.Videos.List {
        return this.videosList(part, chart, videoCategoryId, regionCode, SEARCH_MAX_RESULTS)
    }

    @Throws(IOException::class)
    fun videosList(part: String, chart: String, videoCategoryId: String, regionCode: String, maxResults: Long): YouTube.Videos.List {
        return this.videosList(part, chart, videoCategoryId, regionCode, maxResults, VIDEOS_FIELDS)
    }

    @Throws(IOException::class)
    fun videosList(part: String, chart: String, videoCategoryId: String, regionCode: String, maxResults: Long, fields: String): YouTube.Videos.List {
        if (this.videosListQuery == null) {
            this.videosListQuery = this.youTube.videos().list(part)
                    .setKey(Constants.Key.API_KEY)
        }
        this.videosListQuery!!
                .setChart(chart)
                .setVideoCategoryId(videoCategoryId)
                .setRegionCode(regionCode)
                .setMaxResults(maxResults).fields = fields
        return this.videosListQuery!!
    }

    @Throws(IOException::class)
    fun videosList(part: String, chart: String, videoCategoryId: String, maxResults: Long): YouTube.Videos.List {
        return this.videosList(part, chart, videoCategoryId, maxResults, VIDEOS_FIELDS)
    }

    @Throws(IOException::class)
    fun videosList(part: String, chart: String, videoCategoryId: String, maxResults: Long, fields: String): YouTube.Videos.List {
        if (this.videosListQuery == null) {
            this.videosListQuery = this.youTube.videos().list(part)
                    .setKey(Constants.Key.API_KEY)
        }
        this.videosListQuery!!
                .setChart(chart)
                .setVideoCategoryId(videoCategoryId)
                .setMaxResults(maxResults).fields = fields
        return this.videosListQuery!!
    }

    @Throws(IOException::class)
    fun videoCategoriesList(): YouTube.VideoCategories.List {
        return this.videoCategoriesList(VIDEO_CATEGORIES_PART)
    }

    @Throws(IOException::class)
    fun videoCategoriesList(part: String): YouTube.VideoCategories.List {
        return this.videoCategoriesList(part, VIDEO_CATEGORIES_FIELDS)
    }

    @Throws(IOException::class)
    fun videoCategoriesList(part: String, fields: String): YouTube.VideoCategories.List {
        if (this.videoCategoriesListQuery == null) {
            this.videoCategoriesListQuery = this.youTube.videoCategories().list(part)
                    .setKey(Constants.Key.API_KEY)
        }
        this.videoCategoriesListQuery!!.fields = fields
        return this.videoCategoriesListQuery!!
    }

    @Throws(IOException::class)
    fun videoCategoriesList(part: String, id: String, fields: String): YouTube.VideoCategories.List {
        if (this.videoCategoriesListQuery == null) {
            this.videoCategoriesListQuery = this.youTube.videoCategories().list(part)
                    .setKey(Constants.Key.API_KEY)
        }
        this.videoCategoriesListQuery!!
                .setId(id).fields = fields
        return this.videoCategoriesListQuery!!
    }

    fun requestDetails(videoData: VideoData): VideoData {
        try {
            this.videosList(videoData.id)
        } catch (e: IOException) {
            return videoData
        }

        @SuppressLint("StaticFieldLeak") val task = object : AsyncTask<Void, Void, MutableList<Video>>() {
            override fun doInBackground(vararg params: Void): MutableList<Video> {
                val videoList = ArrayList<Video>()
                try {
                    val response = videosListQuery!!.execute()
                    videoList.addAll(response.items)
                } catch (e: IOException) {
                }

                return videoList
            }
        }
        try {
            val videoList = task.execute().get()
            if (videoList != null && videoList.size > 0) {
                val video = videoList[0]
                videoData.setTo(video)
            }
            return videoData
        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        }

        return videoData
    }

    fun nextAutoplay(currentId: String): VideoData? {
        try {
            this.searchList().relatedToVideoId = currentId
        } catch (e: IOException) {
            return VideoData()
        }

        @SuppressLint("StaticFieldLeak") val task = object : AsyncTask<Void, Void, MutableList<SearchResult>>() {
            override fun doInBackground(vararg params: Void): MutableList<SearchResult> {
                val results = ArrayList<SearchResult>()
                try {
                    val response = searchListQuery!!.execute()
                    results.addAll(response.items)
                } catch (e: IOException) {
                }

                return results
            }
        }
        try {
            val results = task.execute().get()
            val videoData = VideoData()
            for (i in results.indices.reversed()) {
                val result = results[i]
                if (currentId != result.id.videoId) {
                    videoData.setTo(result)
                    return videoData
                }
            }
        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        }

        return null
    }

    fun videoCategories(): MutableList<VideoCategory> {
        try {
            this.videoCategoriesList()
            return this.videoCategoriesListQuery!!.execute().items
        } catch (e: IOException) {
            return ArrayList()
        }

    }

    fun topCharts(): MutableList<VideoData> {
        this.tmpVideoList = ArrayList()
        try {
            this.videosList().chart = "mostPopular"
        } catch (e: IOException) {
            return this.tmpVideoList as ArrayList<VideoData>
        }

        try {
            val results = this.searchListQuery!!.execute().items
            for (i in results.indices) {
                this.tmpVideoList!!.add(VideoData(results[i]))
            }
        } catch (e: IOException) {
        }

        return this.tmpVideoList as ArrayList<VideoData>
    }

    fun searchByCategory(videoCategoryId: String): MutableList<VideoData> {
        this.tmpVideoList = ArrayList()
        try {
            this.searchList().videoCategoryId = videoCategoryId
        } catch (e: IOException) {
            return this.tmpVideoList as ArrayList<VideoData>
        }

        try {
            val results = this.searchListQuery!!.execute().items
            for (i in results.indices) {
                this.tmpVideoList!!.add(VideoData(results[i]))
            }
        } catch (e: IOException) {
        }

        return this.tmpVideoList as ArrayList<VideoData>
    }

    fun searchByTopic(topicId: String): MutableList<VideoData> {
        this.tmpVideoList = ArrayList()
        try {
            this.searchList().topicId = topicId
        } catch (e: IOException) {
            return this.tmpVideoList as ArrayList<VideoData>
        }

        try {
            val results = this.searchListQuery!!.execute().items
            for (i in results.indices) {
                this.tmpVideoList!!.add(VideoData(results[i]))
            }
        } catch (e: IOException) {
        }

        return this.tmpVideoList as ArrayList<VideoData>
    }

    fun search(keywords: String): MutableList<VideoData> {
        this.tmpVideoList = ArrayList()
        try {
            this.searchList().q = keywords
        } catch (e: IOException) {
            return this.tmpVideoList as ArrayList<VideoData>
        }

        try {
            val results = this.searchListQuery!!.execute().items
            for (i in results.indices) {
                this.tmpVideoList!!.add(VideoData(results[i]))
            }
        } catch (e: IOException) {
        }

        return this.tmpVideoList as ArrayList<VideoData>
    }

    companion object {

        private const val SEARCH_MAX_RESULTS = 25L

        private const val SEARCH_FIELDS = "items(id/videoId,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/thumbnails/default/url)"
        private const val VIDEOS_FIELDS = "items(id,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/thumbnails/default/url)"
        private const val VIDEO_CATEGORIES_FIELDS = "items(id,snippet/title)"

        private const val SEARCH_PART = "id,snippet"
        private const val VIDEOS_PART = "id,snippet"
        private const val VIDEO_CATEGORIES_PART = "id,snippet"

        private const val TYPE_VIDEO = "video"
    }

    //    public enum ActionCode {
    //
    //        Activities(),
    //        Captions(),
    //        ChannelBanners(),
    //        Channels(),
    //        ChannelSections(),
    //        Comments(),
    //        CommentThreads(),
    //        FanFundingEvents(),
    //        GuideCategories(),
    //        I18nLanguages(),
    //        I18nRegions(),
    //        LiveBroadcasts(),
    //        LiveChatBans(),
    //        LiveChatMessages(),
    //        LiveChatModerators(),
    //        LiveStreams(),
    //        PlaylistItems(),
    //        Playlists(),
    //        Search(),
    //        Sponsors(),
    //        Subscriptions(),
    //        SuperChatEvents(),
    //        Thumbnails(),
    //        VideoAbuseReportReasons(),
    //        VideoCategories(),
    //        Videos(),
    //        Watermarks()
    //
    //    }

}