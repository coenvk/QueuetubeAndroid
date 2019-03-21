package com.arman.queuetube.modules.playlists;

import android.os.AsyncTask;

import com.arman.queuetube.fragments.PlaylistFragment;
import com.arman.queuetube.model.VideoData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadPlaylistTask extends AsyncTask<String, Integer, List<VideoData>> {

    private PlaylistFragment playlistFragment;

    public LoadPlaylistTask(PlaylistFragment playlistFragment) {
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

    private List<VideoData> createPlaylist(JSONArray array) {
        List<VideoData> videos = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject video = array.getJSONObject(i);
                VideoData videoData = new VideoData(video);
                videos.add(videoData);
            }
        } catch (JSONException e) {

        }
        return videos;
    }

    @Override
    protected List<VideoData> doInBackground(String... strings) {
        JsonArray playlist = GsonPlaylistHelper.getPlaylist(strings[0]);
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
