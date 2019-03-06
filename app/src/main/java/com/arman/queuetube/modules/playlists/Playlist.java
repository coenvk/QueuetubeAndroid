package com.arman.queuetube.modules.playlists;

import com.arman.queuetube.model.VideoData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Playlist {

    private static final String FAVORITES_NAME = "Favorites";

    private List<VideoData> videos;
    private String name;

    public static Playlist favorites() {
        return new Playlist(FAVORITES_NAME);
    }

    public Playlist(String name) {
        this(name, new ArrayList<VideoData>());
    }

    public Playlist(String name, VideoData... videos) {
        this.name = name;
        this.videos = new ArrayList<>(Arrays.asList(videos));
    }

    public Playlist(String name, List<VideoData> videos) {
        this.name = name;
        this.videos = videos;
    }

    public String getName() {
        return name;
    }

    public boolean add(VideoData video) {
        return this.videos.add(video);
    }

    public boolean addAll(Collection<VideoData> videos) {
        return this.videos.addAll(videos);
    }

    public boolean remove(VideoData video) {
        return this.videos.remove(video);
    }

    public VideoData remove(int i) {
        return this.videos.remove(i);
    }

    public int size() {
        return this.videos.size();
    }

    public VideoData get(int i) {
        return this.videos.get(i);
    }

}
