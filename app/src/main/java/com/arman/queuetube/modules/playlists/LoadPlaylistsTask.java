package com.arman.queuetube.modules.playlists;

import android.os.AsyncTask;

import com.arman.queuetube.fragments.PlaylistsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        strings.remove(JSONPlaylistHelper.FAVORITES);
        strings.remove(JSONPlaylistHelper.HISTORY);
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
        JSONArray playlists = JSONPlaylistHelper.getPlaylists();
        List<String> strings = new ArrayList<>();
        try {
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                strings.add(playlist.getString("name"));
            }
        } catch (JSONException e) {

        }
        return strings;
    }
}
