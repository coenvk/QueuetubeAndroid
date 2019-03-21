package com.arman.queuetube.modules.search;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.VideoData;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YouTubeSearcher {

    private static final long SEARCH_MAX_RESULTS = 25L;

    private static final String SEARCH_FIELDS = "items(id/videoId,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/thumbnails/default/url)";
    private static final String VIDEOS_FIELDS = "items(id,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/thumbnails/default/url)";
    private static final String VIDEO_CATEGORIES_FIELDS = "items(id,snippet/title)";

    private static final String SEARCH_PART = "id,snippet";
    private static final String VIDEOS_PART = "id,snippet";
    private static final String VIDEO_CATEGORIES_PART = "id,snippet";

    private static final String TYPE_VIDEO = "video";

    private YouTube youTube;

    private YouTube.Search.List searchListQuery;
    private YouTube.Videos.List videosListQuery;
    private YouTube.VideoCategories.List videoCategoriesListQuery;

    private List<VideoData> tmpVideoList;

    public YouTubeSearcher() {
        this.tmpVideoList = new ArrayList<>();
        this.youTube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("Queuetube").build();
    }

    public YouTube.Search.List searchList() throws IOException {
        return this.searchList(SEARCH_PART);
    }

    public YouTube.Search.List searchList(String part) throws IOException {
        return this.searchList(part, TYPE_VIDEO);
    }

    public YouTube.Search.List searchList(String part, String type) throws IOException {
        return this.searchList(part, type, SEARCH_MAX_RESULTS);
    }

    public YouTube.Search.List searchList(String part, String type, long maxResults) throws IOException {
        return this.searchList(part, type, maxResults, SEARCH_FIELDS);
    }

    public YouTube.Search.List searchList(String part, String type, long maxResults, String fields) throws IOException {
        if (this.searchListQuery == null) {
            this.searchListQuery =
                    this.youTube.search().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.searchListQuery
                .setType(type)
                .setMaxResults(maxResults)
                .setFields(fields);
        return this.searchListQuery;
    }

    public YouTube.Videos.List videosList() throws IOException {
        return this.videosList(VIDEOS_PART);
    }

    public YouTube.Videos.List videosList(long maxResults) throws IOException {
        return this.videosList(VIDEOS_PART, maxResults);
    }

    public YouTube.Videos.List videosList(String part) throws IOException {
        return this.videosList(part, SEARCH_MAX_RESULTS);
    }

    public YouTube.Videos.List videosList(String part, long maxResults) throws IOException {
        if (this.videosListQuery == null) {
            this.videosListQuery =
                    this.youTube.videos().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.videosListQuery
                .setMaxResults(maxResults);
        return this.videosListQuery;
    }

    public YouTube.Videos.List videosList(String part, String id) throws IOException {
        return this.videosList(part, id, VIDEOS_FIELDS);
    }

    public YouTube.Videos.List videosList(String part, String id, String fields) throws IOException {
        return this.videosList(part, id, SEARCH_MAX_RESULTS, fields);
    }

    public YouTube.Videos.List videosList(String part, String id, long maxResults) throws IOException {
        return this.videosList(part, id, maxResults, VIDEOS_FIELDS);
    }

    public YouTube.Videos.List videosList(String part, String id, long maxResults, String fields) throws IOException {
        if (this.videosListQuery == null) {
            this.videosListQuery =
                    this.youTube.videos().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.videosListQuery
                .setId(id)
                .setMaxResults(maxResults)
                .setFields(fields);
        return this.videosListQuery;
    }

    public YouTube.Videos.List videosList(String part, String chart, String videoCategoryId, String regionCode) throws IOException {
        return this.videosList(part, chart, videoCategoryId, regionCode, SEARCH_MAX_RESULTS);
    }

    public YouTube.Videos.List videosList(String part, String chart, String videoCategoryId, String regionCode, long maxResults) throws IOException {
        return this.videosList(part, chart, videoCategoryId, regionCode, maxResults, VIDEOS_FIELDS);
    }

    public YouTube.Videos.List videosList(String part, String chart, String videoCategoryId, String regionCode, long maxResults, String fields) throws IOException {
        if (this.videosListQuery == null) {
            this.videosListQuery =
                    this.youTube.videos().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.videosListQuery
                .setChart(chart)
                .setVideoCategoryId(videoCategoryId)
                .setRegionCode(regionCode)
                .setMaxResults(maxResults)
                .setFields(fields);
        return this.videosListQuery;
    }

    public YouTube.Videos.List videosList(String part, String chart, String videoCategoryId, long maxResults) throws IOException {
        return this.videosList(part, chart, videoCategoryId, maxResults, VIDEOS_FIELDS);
    }

    public YouTube.Videos.List videosList(String part, String chart, String videoCategoryId, long maxResults, String fields) throws IOException {
        if (this.videosListQuery == null) {
            this.videosListQuery =
                    this.youTube.videos().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.videosListQuery
                .setChart(chart)
                .setVideoCategoryId(videoCategoryId)
                .setMaxResults(maxResults)
                .setFields(fields);
        return this.videosListQuery;
    }

    public YouTube.VideoCategories.List videoCategoriesList() throws IOException {
        return this.videoCategoriesList(VIDEO_CATEGORIES_PART);
    }

    public YouTube.VideoCategories.List videoCategoriesList(String part) throws IOException {
        return this.videoCategoriesList(part, VIDEO_CATEGORIES_FIELDS);
    }

    public YouTube.VideoCategories.List videoCategoriesList(String part, String fields) throws IOException {
        if (this.videoCategoriesListQuery == null) {
            this.videoCategoriesListQuery =
                    this.youTube.videoCategories().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.videoCategoriesListQuery
                .setFields(fields);
        return this.videoCategoriesListQuery;
    }

    public YouTube.VideoCategories.List videoCategoriesList(String part, String id, String fields) throws IOException {
        if (this.videoCategoriesListQuery == null) {
            this.videoCategoriesListQuery =
                    this.youTube.videoCategories().list(part)
                            .setKey(Constants.Key.API_KEY);
        }
        this.videoCategoriesListQuery
                .setId(id)
                .setFields(fields);
        return this.videoCategoriesListQuery;
    }

    public VideoData requestDetails(final VideoData videoData) {
        try {
            this.videosList(videoData.getId());
        } catch (IOException e) {
            return videoData;
        }
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<Video>> task = new AsyncTask<Void, Void, List<Video>>() {
            @Override
            protected List<Video> doInBackground(Void... params) {
                List<Video> videoList = new ArrayList<>();
                try {
                    VideoListResponse response = videosListQuery.execute();
                    videoList.addAll(response.getItems());
                } catch (IOException e) {
                }
                return videoList;
            }
        };
        try {
            List<Video> videoList = task.execute().get();
            if (videoList != null && videoList.size() > 0) {
                Video video = videoList.get(0);
                videoData.setTo(video);
            }
            return videoData;
        } catch (InterruptedException | ExecutionException e) {
        }
        return videoData;
    }

    public VideoData nextAutoplay(final String currentId) {
        try {
            this.searchList().setRelatedToVideoId(currentId);
        } catch (IOException e) {
            return new VideoData();
        }
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<SearchResult>> task = new AsyncTask<Void, Void, List<SearchResult>>() {
            @Override
            protected List<SearchResult> doInBackground(Void... params) {
                List<SearchResult> results = new ArrayList<>();
                try {
                    SearchListResponse response = searchListQuery.execute();
                    results.addAll(response.getItems());
                } catch (IOException e) {
                }
                return results;
            }
        };
        try {
            List<SearchResult> results = task.execute().get();
            VideoData videoData = new VideoData();
            for (int i = results.size() - 1; i >= 0; i--) {
                SearchResult result = results.get(i);
                if (!currentId.equals(result.getId().getVideoId())) {
                    videoData.setTo(result);
                    return videoData;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
        }
        return null;
    }

    public List<VideoCategory> videoCategories() {
        try {
            this.videoCategoriesList();
            return this.videoCategoriesListQuery.execute().getItems();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<VideoData> topCharts() {
        this.tmpVideoList = new ArrayList<>();
        try {
            this.videosList().setChart("mostPopular");
        } catch (IOException e) {
            return this.tmpVideoList;
        }
        try {
            List<SearchResult> results = this.searchListQuery.execute().getItems();
            for (int i = 0; i < results.size(); i++) {
                this.tmpVideoList.add(new VideoData(results.get(i)));
            }
        } catch (IOException e) {
        }
        return this.tmpVideoList;
    }

    public List<VideoData> searchByCategory(String videoCategoryId) {
        this.tmpVideoList = new ArrayList<>();
        try {
            this.searchList().setVideoCategoryId(videoCategoryId);
        } catch (IOException e) {
            return this.tmpVideoList;
        }
        try {
            List<SearchResult> results = this.searchListQuery.execute().getItems();
            for (int i = 0; i < results.size(); i++) {
                this.tmpVideoList.add(new VideoData(results.get(i)));
            }
        } catch (IOException e) {
        }
        return this.tmpVideoList;
    }

    public List<VideoData> searchByTopic(String topicId) {
        this.tmpVideoList = new ArrayList<>();
        try {
            this.searchList().setTopicId(topicId);
        } catch (IOException e) {
            return this.tmpVideoList;
        }
        try {
            List<SearchResult> results = this.searchListQuery.execute().getItems();
            for (int i = 0; i < results.size(); i++) {
                this.tmpVideoList.add(new VideoData(results.get(i)));
            }
        } catch (IOException e) {
        }
        return this.tmpVideoList;
    }

    public List<VideoData> search(String keywords) {
        this.tmpVideoList = new ArrayList<>();
        try {
            this.searchList().setQ(keywords);
        } catch (IOException e) {
            return this.tmpVideoList;
        }
        try {
            List<SearchResult> results = this.searchListQuery.execute().getItems();
            for (int i = 0; i < results.size(); i++) {
                this.tmpVideoList.add(new VideoData(results.get(i)));
            }
        } catch (IOException e) {
        }
        return this.tmpVideoList;
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
