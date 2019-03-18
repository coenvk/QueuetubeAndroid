package com.arman.queuetube.modules.playlists;

import android.os.AsyncTask;

import com.arman.queuetube.fragments.DefaultPlaylistFragment;
import com.arman.queuetube.model.VideoData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class LoadPlaylistTask extends AsyncTask<String, Integer, List<VideoData>> {

    private DefaultPlaylistFragment playlistFragment;

    public LoadPlaylistTask(DefaultPlaylistFragment playlistFragment) {
        super();
        this.playlistFragment = playlistFragment;
    }

    private List<VideoData> createPlaylist(JsonArray array) {
        List<VideoData> videos = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject video = array.get(i).getAsJsonObject();
            VideoData videoData = new VideoData(video);
            videos.add(videoData);
        }
        return videos;
    }

    @Override
    protected List<VideoData> doInBackground(String... strings) {
        JsonArray playlist = PlaylistHelper.getPlaylist(strings[0]);
        return this.createPlaylist(playlist);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<VideoData> videoData) {
        super.onPostExecute(videoData);
        this.playlistFragment.getPlaylistAdapter().setAll(videoData);
        this.playlistFragment.finishRefresh();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(List<VideoData> videoData) {
        super.onCancelled(videoData);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
