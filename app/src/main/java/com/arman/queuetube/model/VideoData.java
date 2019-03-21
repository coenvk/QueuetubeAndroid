package com.arman.queuetube.model;

import android.text.Html;

import com.arman.queuetube.config.Constants;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoStatistics;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

public class VideoData {

    private String title;
    private String thumbnailUrl;
    private String id;
    private String publishedOn;
    private String channel;

    private boolean favorited;

    public VideoData() {

    }

    public VideoData(String id) {
        this.id = id;
    }

    public VideoData(String title, String thumbnailUrl, String id, String publishedOn, String channel, String description) {
        this.setTitle(title);
        this.thumbnailUrl = thumbnailUrl;
        this.id = id;
        this.publishedOn = publishedOn;
        this.channel = channel;
    }

    public VideoData(SearchResult result) {
        this.setTo(result);
    }

    public VideoData(Video video) {
        this.setTo(video);
    }

    public VideoData(JsonObject object) {
        this.setTo(object);
    }

    public VideoData(JSONObject object) {
        this.setTo(object);
    }

    public void setTo(SearchResult result) {
        this.setTitle(result.getSnippet().getTitle());
        this.thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
        this.id = result.getId().getVideoId();
        this.publishedOn = result.getSnippet().getPublishedAt().toString();
        this.channel = result.getSnippet().getChannelTitle();
    }

    public void setTo(Video video) {
        this.setTitle(video.getSnippet().getTitle());
        this.thumbnailUrl = video.getSnippet().getThumbnails().getDefault().getUrl();
        this.id = video.getId();
        this.publishedOn = video.getSnippet().getPublishedAt().toString();
        this.channel = video.getSnippet().getChannelTitle();
    }

    public void setTo(JsonObject object) {
        this.setTitle(object.get(Constants.VideoData.TITLE).getAsString());
        this.thumbnailUrl = object.get(Constants.VideoData.THUMBNAIL_URL).getAsString();
        this.id = object.get(Constants.VideoData.ID).getAsString();
        this.publishedOn = object.get(Constants.VideoData.PUBLISHED_ON).getAsString();
        this.channel = object.get(Constants.VideoData.CHANNEL).getAsString();
    }

    public void setTo(JSONObject object) {
        try {
            this.setTitle(object.getString(Constants.VideoData.TITLE));
            this.thumbnailUrl = object.getString(Constants.VideoData.THUMBNAIL_URL);
            this.id = object.getString(Constants.VideoData.ID);
            this.publishedOn = object.getString(Constants.VideoData.PUBLISHED_ON);
            this.channel = object.getString(Constants.VideoData.CHANNEL);
        } catch (JSONException e) {
        }
    }

    public void setTo(VideoData videoData) {
        this.setTitle(videoData.getTitle());
        this.setThumbnailUrl(videoData.getThumbnailUrl());
        this.setId(videoData.getId());
        this.setChannel(videoData.getChannel());
        this.setPublishedOn(videoData.getPublishedOn());
        this.setFavorited(videoData.isFavorited());
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT).toString();
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof VideoData) {
            VideoData other = (VideoData) o;
            return other.getId().equals(this.getId());
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + this.id + "]: " + this.title;
    }

}
