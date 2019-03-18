package com.arman.queuetube.modules.playlists;

import android.os.AsyncTask;

import com.arman.queuetube.fragments.PlaylistsFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class LoadPlaylistsTask extends AsyncTask<Void, Integer, List<String>> {

    private PlaylistsFragment playlistsFragment;

    public LoadPlaylistsTask(PlaylistsFragment playlistsFragment) {
        super();
        this.playlistsFragment = playlistsFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        strings.remove(PlaylistHelper.FAVORITES);
        strings.remove(PlaylistHelper.HISTORY);
        this.playlistsFragment.getPlaylistsAdapter().setAll(strings);
        this.playlistsFragment.finishRefresh();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(List<String> strings) {
        super.onCancelled(strings);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        JsonArray playlists = PlaylistHelper.getPlaylists();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < playlists.size(); i++) {
            JsonObject playlist = playlists.get(i).getAsJsonObject();
            strings.add(playlist.get("name").getAsString());
        }
        return strings;
    }
}
