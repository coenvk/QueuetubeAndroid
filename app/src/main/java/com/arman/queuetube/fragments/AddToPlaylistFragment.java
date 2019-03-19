package com.arman.queuetube.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.modules.playlists.JSONPlaylistHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddToPlaylistFragment extends DialogFragment {

    private List<Integer> selectedItems;
    private CharSequence[] playlists;
    private boolean[] checked;

    private VideoData video;

    @SuppressLint("StaticFieldLeak")
    private AsyncTask<Void, Integer, CharSequence[]> loadPlaylistsTask = new AsyncTask<Void, Integer, CharSequence[]>() {
        @Override
        protected CharSequence[] doInBackground(Void... voids) {
            JSONArray playlists = JSONPlaylistHelper.getUserPlaylists();
            CharSequence[] strings = new CharSequence[playlists.length()];
            checked = new boolean[strings.length];
            try {
                for (int i = 0; i < strings.length; i++) {
                    JSONObject obj = playlists.getJSONObject(i);
                    strings[i] = obj.getString("name");
                    JSONArray playlist = obj.getJSONArray("playlist");
                    for (int j = 0; j < playlist.length(); j++) {
                        JSONObject vid = playlist.getJSONObject(j);
                        if (vid.getString("id").equals(video.getId())) {
                            checked[i] = true;
                            break;
                        }
                    }
                }
            } catch (JSONException e) {

            }
            return strings;
        }
    };

    public void setVideo(VideoData video) {
        this.video = video;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.selectedItems = new ArrayList<>();
        try {
            this.playlists = this.loadPlaylistsTask.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setTitle("Error")
                    .setMessage("An error occurred while trying to retrieve your playlists")
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            return builder.create();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Save video to...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (video != null) {
                            for (int j = 0; j < AddToPlaylistFragment.this.playlists.length; j++) {
                                String name = AddToPlaylistFragment.this.playlists[j].toString();
                                if (AddToPlaylistFragment.this.selectedItems.contains(j)) {
                                    JSONPlaylistHelper.writeTo(name, video);
                                } else {
                                    JSONPlaylistHelper.removeFrom(name, video);
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
                .setMultiChoiceItems(this.playlists, this.checked, new DialogInterface.OnMultiChoiceClickListener() {
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

}
