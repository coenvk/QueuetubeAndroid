package com.arman.queuetube.modules.playlists;

import android.content.Context;

import com.arman.queuetube.model.VideoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class PlaylistHelper {

    private static final String STORAGE_FILE_NAME = "playlists.json";

    private Context context;
    private BufferedReader br;
    private BufferedWriter bw;

    public PlaylistHelper(Context context) {
        this.context = context;
        this.onCreate();
    }

    public void onCreate() {
        boolean fileExists = this.doesFileExist();
        try {
            FileInputStream fis = context.openFileInput(STORAGE_FILE_NAME);
            this.br = new BufferedReader(new InputStreamReader(fis));

            FileOutputStream fos = context.openFileOutput(STORAGE_FILE_NAME, Context.MODE_PRIVATE);
            this.bw = new BufferedWriter(new OutputStreamWriter(fos));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!fileExists) {
            JSONObject root = new JSONObject();
            try {
                root.put("playlists", new JSONArray());
                this.bw.write(root.toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        try {
            this.br.close();
            this.bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject newPlaylist(String name) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("playlist", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private JSONObject createPlaylist(Playlist playlist) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", playlist.getName());
            JSONArray array = new JSONArray();
            for (int i = 0; i < playlist.size(); i++) {
                array.put(createVideo(playlist.get(i)));
            }
            obj.put("playlist", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private JSONObject createVideo(VideoData video) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", video.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private JSONArray getPlaylists(JSONObject root) {
        try {
            assert root != null;
            return root.getJSONArray("playlists");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray getPlaylist(JSONObject root, String name) {
        JSONArray playlists = this.getPlaylists(root);
        try {
            assert playlists != null;
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject obj = playlists.getJSONObject(i);
                if (obj.getString("name").equals(name)) {
                    return obj.getJSONArray("playlist");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeNew(String name) {
        try {
            JSONObject root = this.read();
            assert root != null;
            JSONArray playlists = this.getPlaylists(root);
            assert playlists != null;

            JSONObject obj = this.newPlaylist(name);

            playlists.put(obj);
            this.bw.write(root.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean writeTo(String name, VideoData video) {
        try {
            JSONObject root = this.read();
            assert root != null;
            JSONArray playlist = this.getPlaylist(root, name);
            if (playlist != null) {
                playlist.put(createVideo(video));
                this.bw.write(root.toString());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JSONObject read() {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = this.br.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean write(JSONObject obj) {
        try {
            String json = obj.toString();
            this.bw.write(json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean doesFileExist() {
        String path = context.getFilesDir().getAbsolutePath() + "/" + STORAGE_FILE_NAME;
        File file = new File(path);
        return file.exists();
    }

}
