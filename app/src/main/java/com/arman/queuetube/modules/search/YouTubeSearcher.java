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
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YouTubeSearcher {

    private YouTube youTube;
    private YouTube.Search.List relatedQuery;
    private YouTube.Search.List query;
    private YouTube.Videos.List videoRequest;

    private List<VideoData> tempList;

    public YouTubeSearcher() {
        this.tempList = new ArrayList<>();
        this.youTube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("Queuetube").build();
    }

    private boolean initQuery() {
        try {
            query = youTube.search().list("id,snippet");
            query.setKey(Constants.Key.API_KEY);
            query.setType("video");
            query.setMaxResults(10L);
            query.setFields("items(id/videoId,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/description,snippet/thumbnails/default/url)");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean initRelatedQuery() {
        try {
            relatedQuery = youTube.search().list("id,snippet");
            relatedQuery.setKey(Constants.Key.API_KEY);
            relatedQuery.setMaxResults(5L);
            relatedQuery.setFields("items(id/videoId,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/description,snippet/thumbnails/default/url)");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean initVideoRequest() {
        try {
            videoRequest = youTube.videos().list("id,snippet,statistics");
            videoRequest.setFields("items(id,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/description,snippet/thumbnails/default/url,statistics)");
            videoRequest.setKey(Constants.Key.API_KEY);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public VideoData requestDetails(final VideoData videoData) {
        if (this.videoRequest == null) {
            if (!this.initVideoRequest()) {
                return videoData;
            }
        }
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<Video>> task = new AsyncTask<Void, Void, List<Video>>() {
            @Override
            protected List<Video> doInBackground(Void... params) {
                List<Video> videoList = new ArrayList<>();
                try {
                    videoRequest.setId(videoData.getId());
                    VideoListResponse response = videoRequest.execute();
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
        if (this.relatedQuery == null) {
            if (!this.initRelatedQuery()) {
                return new VideoData();
            }
        }
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<SearchResult>> task = new AsyncTask<Void, Void, List<SearchResult>>() {
            @Override
            protected List<SearchResult> doInBackground(Void... params) {
                relatedQuery.setRelatedToVideoId(currentId);
                List<SearchResult> results = new ArrayList<>();
                try {
                    SearchListResponse response = query.execute();
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

    public List<VideoData> search(String keywords) {
        this.tempList.clear();
        if (this.query == null) {
            if (!this.initQuery()) {
                return this.tempList;
            }
        }
        query.setQ(keywords);
        List<SearchResult> results = new ArrayList<>();
        try {
            SearchListResponse response = query.execute();
            results.addAll(response.getItems());
        } catch (IOException e) {
        }
        for (int i = 0; i < results.size(); i++) {
            this.tempList.add(new VideoData(results.get(i)));
        }
        return this.tempList;
    }

}
