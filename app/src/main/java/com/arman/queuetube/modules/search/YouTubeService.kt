package com.arman.queuetube.modules.search

import com.arman.queuetube.config.Constants
import com.arman.queuetube.modules.VideoCall
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeService {

    companion object {

        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        const val MAX_RESULTS = 25

        const val SEARCH_FIELDS = "items(id/videoId,snippet/title,snippet/channelTitle,snippet/publishedAt)"
        const val VIDEOS_FIELDS = "items(id,snippet/title,snippet/channelTitle,snippet/publishedAt)"

        const val SEARCH_PART = "id,snippet"
        const val VIDEOS_PART = "id,snippet"

        const val TYPE_VIDEO = "video"

        private val instance: YouTubeService by lazy {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
            retrofit.create(YouTubeService::class.java)
        }

        fun get(): YouTubeService {
            return instance
        }

    }

    @GET("videos?key=${Constants.Key.API_KEY}&part=$VIDEOS_PART&maxResults=$MAX_RESULTS&fields=$VIDEOS_FIELDS&chart=mostPopular")
    fun topCharts(@Query("regionCode") regionCode: String = "US", @Query("videoCategoryId") videoCategoryId: String = "0"): VideoCall

    fun topMusicCharts(@Query("regionCode") regionCode: String = "US"): VideoCall = topCharts(regionCode, "10")

    @GET("search?key=${Constants.Key.API_KEY}&part=$SEARCH_PART&type=$TYPE_VIDEO&maxResults=$MAX_RESULTS&fields=$SEARCH_FIELDS&eventType=live")
    fun searchLive(@Query("videoCategoryId") videoCategoryId: String?): VideoCall

    fun searchLiveMusic(): VideoCall = searchLive("10")

    @GET("search?key=${Constants.Key.API_KEY}&part=$SEARCH_PART&type=$TYPE_VIDEO&maxResults=$MAX_RESULTS&fields=$SEARCH_FIELDS")
    fun search(@Query("q") q: String): VideoCall

    @GET("search?key=${Constants.Key.API_KEY}&part=$SEARCH_PART&type=$TYPE_VIDEO&maxResults=$MAX_RESULTS&fields=$SEARCH_FIELDS")
    fun recommendations(@Query("relatedToVideoId") relatedToVideoId: String): VideoCall

}