package com.arman.queuetube.model;

import android.text.Html;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoStatistics;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;

public class VideoData {

    private String title;
    private String thumbnailUrl;
    private String id;
    private String publishedOn;
    private String channel;
    private String description;
    private int views;
    private int likes;
    private int dislikes;

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
        this.description = description;
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

    public void setTo(SearchResult result) {
        this.setTitle(result.getSnippet().getTitle());
        this.thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
        this.id = result.getId().getVideoId();
        this.publishedOn = result.getSnippet().getPublishedAt().toString();
        this.channel = result.getSnippet().getChannelTitle();
        this.description = result.getSnippet().getDescription();
    }

    public void setTo(Video video) {
        this.setTitle(video.getSnippet().getTitle());
        this.thumbnailUrl = video.getSnippet().getThumbnails().getDefault().getUrl();
        this.id = video.getId();
        this.publishedOn = video.getSnippet().getPublishedAt().toString();
        this.channel = video.getSnippet().getChannelTitle();
        this.description = video.getSnippet().getDescription();
        this.setStatistics(video.getStatistics());
    }

    public void setTo(JsonObject object) {
        this.setTitle(object.get("title").getAsString());
        this.thumbnailUrl = object.get("thumbnailUrl").getAsString();
        this.id = object.get("id").getAsString();
        this.publishedOn = object.get("publishedOn").getAsString();
        this.channel = object.get("channel").getAsString();
        this.description = object.get("description").getAsString();
        this.views = object.get("views").getAsInt();
        this.likes = object.get("likes").getAsInt();
        this.dislikes = object.get("dislikes").getAsInt();
    }

    public void setTo(VideoData videoData) {
        this.setTitle(videoData.getTitle());
        this.setThumbnailUrl(videoData.getThumbnailUrl());
        this.setDescription(videoData.getDescription());
        this.setId(videoData.getId());
        this.setChannel(videoData.getChannel());
        this.setPublishedOn(videoData.getPublishedOn());
        this.setViews(videoData.getViews());
        this.setLikes(videoData.getLikes());
        this.setDislikes(videoData.getDislikes());
        this.setFavorited(videoData.isFavorited());
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
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

    public void setStatistics(VideoStatistics statistics) {
        this.setViews(statistics.getViewCount().intValue());
        this.setLikes(statistics.getLikeCount().intValue());
        this.setDislikes(statistics.getDislikeCount().intValue());
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + this.id + "]: " + this.title;
    }

}
