package com.arman.queuetube.modules.search;

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

        if (this.query == null) {
            if (!this.initQuery()) {
                return;
            }
        }

        if (this.relatedQuery == null) {
            if (!this.initRelatedQuery()) {
                return;
            }
        }

        if (this.videoRequest == null) {
            if (!this.initVideoRequest()) {
                return;
            }
        }
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
                return new VideoData();
            }
        }
        try {
            videoRequest.setId(videoData.getId());
            VideoListResponse response = videoRequest.execute();
            Video video = response.getItems().get(0);
            videoData.setTo(video);
        } catch (IOException e) {
        }
        return videoData;
    }

    public VideoData nextAutoplay(final String currentId) {
        if (this.relatedQuery == null) {
            if (!this.initRelatedQuery()) {
                return new VideoData();
            }
        }
        relatedQuery.setRelatedToVideoId(currentId);
        List<SearchResult> results;
        try {
            SearchListResponse response = query.execute();
            results = response.getItems();
            VideoData videoData = new VideoData();
            for (int i = results.size() - 1; i >= 0; i--) {
                SearchResult result = results.get(i);
                if (!currentId.equals(result.getId().getVideoId())) {
                    videoData.setTo(result);
                    return videoData;
                }
            }
        } catch (IOException e) {
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
