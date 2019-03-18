package com.arman.queuetube.modules.playlists;

import android.content.Context;
import android.os.AsyncTask;

import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.model.VideoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class PlaylistHelper {

    public static final String FAVORITES = "Favorites";
    public static final String HISTORY = "History";
    private static final String STORAGE_FILE_NAME = "playlists.json";
    private static Context context;
    private static SavePlaylistsTask savePlaylistsTask;

    private static void executeSave(String string) {
        AsyncTask.Status status = savePlaylistsTask.getStatus();
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                savePlaylistsTask = new SavePlaylistsTask((MainActivity) context);
            }
            savePlaylistsTask.execute(string);
        }
    }

    public static void doWrite(String string) throws IOException {
        FileOutputStream fos = context.openFileOutput(STORAGE_FILE_NAME, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(string);
        bw.flush();
        bw.close();
    }

    private static String doRead() throws IOException {
        FileInputStream fis = context.openFileInput(STORAGE_FILE_NAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static void onCreate(Context context) {
        PlaylistHelper.context = context;
        savePlaylistsTask = new SavePlaylistsTask((MainActivity) context);
        boolean fileExists = doesFileExist();
        if (!fileExists) {
            JSONObject root = new JSONObject();
            try {
                JSONArray playlists = new JSONArray();
                playlists.put(newPlaylist(PlaylistHelper.HISTORY));
                playlists.put(newPlaylist(PlaylistHelper.FAVORITES));

                root.put("playlists", playlists);
                executeSave(root.toString());
            } catch (JSONException e) {

            }
        }
    }

    public static JSONArray getFavorites() {
        return getPlaylist(read(), PlaylistHelper.FAVORITES);
    }

    public static JSONArray getHistory() {
        return getPlaylist(read(), PlaylistHelper.HISTORY);
    }

    public static boolean isFavorited(VideoData video) {
        JSONArray favorites = getFavorites();
        try {
            for (int i = 0; i < favorites.length(); i++) {
                JSONObject obj = favorites.getJSONObject(i);
                String id = obj.getString("id");
                if (id.equals(video.getId())) {
                    return true;
                }
            }
        } catch (JSONException e) {

        }
        return false;
    }

    private static JSONObject newPlaylist(String name) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("playlist", new JSONArray());
        } catch (JSONException e) {

        }
        return obj;
    }

    private static JSONObject createVideo(VideoData video) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", video.getTitle());
            obj.put("thumbnailUrl", video.getThumbnailUrl());
            obj.put("id", video.getId());
            obj.put("publishedOn", video.getPublishedOn());
            obj.put("channel", video.getChannel());
            obj.put("description", video.getDescription());
            obj.put("views", video.getViews());
            obj.put("likes", video.getLikes());
            obj.put("dislikes", video.getDislikes());
        } catch (JSONException e) {

        }
        return obj;
    }

    public static JSONArray getPlaylists() {
        return getPlaylists(read());
    }

    private static JSONArray getPlaylists(JSONObject root) {
        try {
            return root.getJSONArray("playlists");
        } catch (JSONException e) {

        }
        return null;
    }

    public static JSONArray getPlaylist(String name) {
        JSONObject root = read();
        assert root != null;
        return getPlaylist(root, name);
    }

    private static JSONArray getPlaylist(JSONObject root, String name) {
        JSONArray playlists = getPlaylists(root);
        try {
            assert playlists != null;
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject obj = playlists.getJSONObject(i);
                if (obj.getString("name").equals(name)) {
                    return obj.getJSONArray("playlist");
                }
            }
        } catch (JSONException e) {

        }
        return null;
    }

    public static boolean writeNew(String name) {
        JSONObject root = read();
        assert root != null;
        JSONArray playlists = getPlaylists(root);
        assert playlists != null;

        JSONObject obj = newPlaylist(name);

        playlists.put(obj);
        executeSave(root.toString());
        return true;
    }

    private static boolean insert(JSONArray array, int pos, Object obj) {
        try {
            for (int i = array.length(); i > pos; i--) {
                array.put(i, array.get(i - 1));
            }
            array.put(pos, obj);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean reorder(String name, int fromIndex, int toIndex) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            Object video = playlist.remove(fromIndex);
            System.out.println(video);
            System.out.println(fromIndex + " -> " + toIndex);
            if (!insert(playlist, toIndex, video)) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean reorder(String name, VideoData video, int toIndex) {
        try {
            JSONObject root = read();
            JSONArray playlist = getPlaylist(root, name);
            if (playlist != null) {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = (JSONObject) playlist.remove(i);
                    if (obj.get("id").equals(video.getId())) {
                        if (!insert(playlist, toIndex, video)) {
                            return false;
                        }
                        executeSave(root.toString());
                        return true;
                    }
                }
            }
        } catch (JSONException e) {

        }
        return false;
    }

    public static boolean removeFrom(String name, VideoData video) {
        try {
            JSONObject root = read();
            JSONArray playlist = getPlaylist(root, name);
            if (playlist != null) {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = playlist.getJSONObject(i);
                    if (obj.get("id").equals(video.getId())) {
                        playlist.remove(i);
                        executeSave(root.toString());
                        return true;
                    }
                }
            }
        } catch (JSONException e) {

        }
        return false;
    }

    public static boolean writeToNew(String name, VideoData video) {
        try {
            JSONObject root = read();
            JSONArray playlists = getPlaylists(root);
            assert playlists != null;

            JSONArray playlist = getPlaylist(root, name);
            if (playlist == null) {
                JSONObject obj = newPlaylist(name);
                playlists.put(obj);
                playlist = obj.getJSONArray("playlist");
            }
            if (playlist != null) {
                playlist.put(createVideo(video));
                executeSave(root.toString());
                return true;
            }
        } catch (JSONException e) {

        }
        return false;
    }

    public static boolean writeTo(String name, VideoData video, int index) {
        JSONObject root = read();
        assert root != null;
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            if (!insert(playlist, index, createVideo(video))) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeTo(String name, VideoData video) {
        JSONObject root = read();
        assert root != null;
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            playlist.put(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static JSONObject read() {
        try {
            return new JSONObject(doRead());
        } catch (JSONException | IOException e) {

        }
        return null;
    }

    private static boolean write(JSONObject obj) {
        String json = obj.toString();
        executeSave(json);
        return true;
    }

    private static boolean doesFileExist() {
        String path = context.getFilesDir().getAbsolutePath() + "/" + STORAGE_FILE_NAME;
        File file = new File(path);
        return file.exists();
    }

    public static boolean remove(String name) {
        try {
            JSONObject root = read();
            JSONArray playlists = getPlaylists(root);
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                if (playlist.getString("name").equals(name)) {
                    playlists.remove(i);
                    executeSave(root.toString());
                    return true;
                }
            }

        } catch (JSONException e) {

        }
        return false;
    }

    public static boolean clear(String name) {
        try {
            JSONObject root = read();
            JSONArray playlists = getPlaylists(root);
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                if (playlist.getString("name").equals(name)) {
                    playlists.remove(i);
                    playlists.put(newPlaylist(name));
                    executeSave(root.toString());
                    return true;
                }
            }

        } catch (JSONException e) {

        }
        return false;
    }

}
