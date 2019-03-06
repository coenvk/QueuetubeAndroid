package com.arman.queuetube.model;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoStatistics;

import java.util.ArrayList;
import java.util.List;

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

    public VideoData() {

    }

    public VideoData(String title, String thumbnailUrl, String id, String publishedOn, String channel, String description) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.id = id;
        this.publishedOn = publishedOn;
        this.channel = channel;
        this.description = description;
    }

    public VideoData(SearchResult result) {
        this.setTo(result);
    }

    public void setTo(SearchResult result) {
        this.title = result.getSnippet().getTitle();
        this.thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
        this.id = result.getId().getVideoId();
        this.publishedOn = result.getSnippet().getPublishedAt().toString();
        this.channel = result.getSnippet().getChannelTitle();
        this.description = result.getSnippet().getDescription();
    }

    public List<String> asList() {
        List<String> strings = new ArrayList<>();
        strings.add(this.description);
        strings.add(this.publishedOn);
        strings.add(this.channel);
        strings.add(String.valueOf(this.views));
        strings.add(String.valueOf(this.likes));
        strings.add(String.valueOf(this.dislikes));
        return strings;
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
        this.title = title;
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

}
