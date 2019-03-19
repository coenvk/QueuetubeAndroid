package com.arman.queuetube.modules.playlists;

import android.os.AsyncTask;

import com.arman.queuetube.activities.MainActivity;

import java.io.IOException;

public class SavePlaylistsTask extends AsyncTask<String, Integer, Void> {

    private MainActivity activity;

    public SavePlaylistsTask(MainActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            JSONPlaylistHelper.doWrite(strings[0]);
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.activity.refreshPlaylists();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
