package com.arman.queuetube.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.modules.playlists.GsonPlaylistHelper;
import com.arman.queuetube.util.Tuple;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddToPlaylistFragment extends DialogFragment {

    private List<Integer> selectedItems;
    private VideoData video;

    @SuppressLint("StaticFieldLeak")
    private AsyncTask<Void, Integer, Tuple<CharSequence[], boolean[]>> loadPlaylistsTask = new AsyncTask<Void, Integer, Tuple<CharSequence[], boolean[]>>() {
        @Override
        protected Tuple<CharSequence[], boolean[]> doInBackground(Void... voids) {
            JsonArray playlists = GsonPlaylistHelper.getUserPlaylists();
            return loadPlaylists(playlists);
        }
    };

    private Tuple<CharSequence[], boolean[]> loadPlaylists(JsonArray array) {
        CharSequence[] strings = new CharSequence[array.size()];
        boolean[] checked = new boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            strings[i] = obj.getAsJsonPrimitive(Constants.Json.Key.NAME).toString();
            JsonArray playlist = obj.getAsJsonArray(Constants.Json.Key.PLAYLIST);
            for (int j = 0; j < playlist.size(); j++) {
                JsonObject vid = playlist.get(j).getAsJsonObject();
                if (vid.get(Constants.Json.Key.ID).getAsString().equals(video.getId())) {
                    checked[i] = true;
                    break;
                }
            }
        }
        return new Tuple<>(strings, checked);
    }

    private Tuple<CharSequence[], boolean[]> loadPlaylists(JSONArray array) {
        CharSequence[] strings = new CharSequence[array.length()];
        boolean[] checked = new boolean[strings.length];
        try {
            for (int i = 0; i < strings.length; i++) {
                JSONObject obj = array.getJSONObject(i);
                strings[i] = obj.getString(Constants.Json.Key.NAME);
                JSONArray playlist = obj.getJSONArray(Constants.Json.Key.PLAYLIST);
                for (int j = 0; j < playlist.length(); j++) {
                    JSONObject vid = playlist.getJSONObject(j);
                    if (vid.getString(Constants.Json.Key.ID).equals(video.getId())) {
                        checked[i] = true;
                        break;
                    }
                }
            }
        } catch (JSONException e) {

        }
        return new Tuple<>(strings, checked);
    }

    public void setVideo(VideoData video) {
        this.video = video;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.selectedItems = new ArrayList<>();
        Tuple<CharSequence[], boolean[]> tuple;
        try {
            tuple = this.loadPlaylistsTask.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setTitle("Error")
                    .setMessage("An error occurred while trying to retrieve your playlists")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            return builder.create();
        }

        final CharSequence[] playlists = tuple.getLeft();
        final boolean[] checked = tuple.getRight();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Save video to...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (video != null) {
                            for (int j = 0; j < playlists.length; j++) {
                                String name = playlists[j].toString();
                                if (AddToPlaylistFragment.this.selectedItems.contains(j)) {
                                    GsonPlaylistHelper.writeTo(name, video);
                                } else {
                                    GsonPlaylistHelper.removeFrom(name, video);
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setMultiChoiceItems(playlists, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            AddToPlaylistFragment.this.selectedItems.add(i);
                        } else if (AddToPlaylistFragment.this.selectedItems.contains(i)) {
                            AddToPlaylistFragment.this.selectedItems.remove(Integer.valueOf(i));
                        }
                    }
                });
        return builder.create();
    }

    public interface OnDialogDismissListener {

        public void onDialogAccept(CharSequence[] playlists, Collection<Integer> selectedItems);

        public void onDialogCancel();

    }

}
