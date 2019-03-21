package com.arman.queuetube.modules.playlists;

import android.os.AsyncTask;

import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.PlaylistsFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
        strings.remove(Constants.Json.Playlist.FAVORITES);
        strings.remove(Constants.Json.Playlist.HISTORY);
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

    private List<String> getNames(JsonArray array) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject playlist = array.get(i).getAsJsonObject();
            strings.add(playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).toString());
        }
        return strings;
    }

    private List<String> getNames(JSONArray array) {
        List<String> strings = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject playlist = array.getJSONObject(i);
                strings.add(playlist.getString(Constants.Json.Key.NAME));
            }
        } catch (JSONException e) {

        }
        return strings;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        JsonArray playlists = GsonPlaylistHelper.getPlaylists();
        return getNames(playlists);
    }
}
