package com.arman.queuetube.modules.playlists;

import android.content.Context;
import android.os.AsyncTask;

import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.config.Constants;
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

public class JSONPlaylistHelper {

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
        FileOutputStream fos = context.openFileOutput(Constants.Json.STORAGE_FILE_NAME, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(string);
        bw.flush();
        bw.close();
    }

    private static String doRead() throws IOException {
        FileInputStream fis = context.openFileInput(Constants.Json.STORAGE_FILE_NAME);
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
        JSONPlaylistHelper.context = context;
        savePlaylistsTask = new SavePlaylistsTask((MainActivity) context);
        boolean fileExists = doesFileExist();
        try {
            if (!fileExists) {
                JSONObject root = new JSONObject();
                JSONArray playlists = new JSONArray();
                playlists.put(newPlaylist(Constants.Json.Playlist.HISTORY));
                playlists.put(newPlaylist(Constants.Json.Playlist.FAVORITES));

                root.put(Constants.Json.Key.PLAYLISTS, playlists);
                executeSave(root.toString());
            }
        } catch (JSONException e) {

        }
    }

    public static JSONArray getFavorites() {
        return getPlaylist(read(), Constants.Json.Playlist.FAVORITES);
    }

    public static JSONArray getHistory() {
        return getPlaylist(read(), Constants.Json.Playlist.HISTORY);
    }

    public static boolean isFavorited(VideoData video) {
        JSONArray favorites = getFavorites();
        try {
            for (int i = 0; i < favorites.length(); i++) {
                JSONObject obj = favorites.getJSONObject(i);
                String id = obj.getString(Constants.Json.Key.ID);
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
            obj.put(Constants.Json.Key.NAME, name);
            obj.put(Constants.Json.Key.PLAYLIST, new JSONArray());
        } catch (JSONException e) {

        }
        return obj;
    }

    private static JSONObject createVideo(VideoData video) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.Json.Key.ID, video.getId());
            obj.put(Constants.VideoData.TITLE, video.getTitle());
            obj.put(Constants.VideoData.THUMBNAIL_URL, video.getThumbnailUrl());
            obj.put(Constants.VideoData.CHANNEL, video.getChannel());
            obj.put(Constants.VideoData.PUBLISHED_ON, video.getPublishedOn());
        } catch (JSONException e) {

        }
        return obj;
    }

    public static JSONArray getPlaylists() {
        return getPlaylists(read());
    }

    private static JSONArray getPlaylists(JSONObject root) {
        try {
            return root.getJSONArray(Constants.Json.Key.PLAYLISTS);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public static JSONArray getUserPlaylists() {
        JSONObject root = read();
        JSONArray playlists = getPlaylists(root);
        JSONArray userPlaylists = new JSONArray();
        try {
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject obj = playlists.getJSONObject(i);
                String name = obj.getString(Constants.Json.Key.NAME);
                if (!name.equals(Constants.Json.Playlist.FAVORITES) && !name.equals(Constants.Json.Playlist.HISTORY)) {
                    userPlaylists.put(obj);
                }
            }
        } catch (JSONException e) {

        }
        return userPlaylists;
    }

    public static JSONArray getPlaylist(String name) {
        JSONObject root = read();
        return getPlaylist(root, name);
    }

    private static JSONArray getPlaylist(JSONObject root, String name) {
        JSONArray playlists = getPlaylists(root);
        try {
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject obj = playlists.getJSONObject(i);
                if (obj.getString(Constants.Json.Key.NAME).equals(name)) {
                    return obj.getJSONArray(Constants.Json.Key.PLAYLIST);
                }
            }
        } catch (JSONException e) {

        }
        return null;
    }

    public static boolean writeNewIfNotFound(String name) {
        JSONObject root = read();
        JSONArray playlists = getPlaylists(root);

        try {
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                if (playlist.getString(Constants.Json.Key.NAME).equals(name)) {
                    return false;
                }
            }
        } catch (JSONException e) {
            return false;
        }

        JSONObject obj = newPlaylist(name);

        playlists.put(obj);
        executeSave(root.toString());
        return true;
    }

    public static boolean writeNew(String name) {
        JSONObject root = read();
        JSONArray playlists = getPlaylists(root);

        JSONObject obj = newPlaylist(name);

        playlists.put(obj);
        executeSave(root.toString());
        return true;
    }

    private static boolean insert(JSONArray array, int pos, JSONObject obj) {
        try {
            for (int i = array.length(); i > pos; i--) {
                array.put(i, array.get(i - 1));
            }
            array.put(pos, obj);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public static boolean reorder(String name, int fromIndex, int toIndex) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            JSONObject video = (JSONObject) playlist.remove(fromIndex);
            if (!insert(playlist, toIndex, video)) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean reorder(String name, VideoData video, int toIndex) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            try {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = playlist.getJSONObject(i);
                    if (obj.getString(Constants.Json.Key.ID).equals(video.getId())) {
                        playlist.remove(i);
                        if (!insert(playlist, toIndex, obj)) {
                            return false;
                        }
                        executeSave(root.toString());
                        return true;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean removeFrom(String name, VideoData video) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            try {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = playlist.getJSONObject(i);
                    if (obj.getString(Constants.Json.Key.ID).equals(video.getId())) {
                        playlist.remove(i);
                        executeSave(root.toString());
                        return true;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean writeToNew(String name, VideoData video) {
        JSONObject root = read();
        JSONArray playlists = getPlaylists(root);

        JSONArray playlist = getPlaylist(root, name);
        if (playlist == null) {
            try {
                JSONObject obj = newPlaylist(name);
                playlists.put(obj);
                playlist = obj.getJSONArray(Constants.Json.Key.PLAYLIST);
            } catch (JSONException e) {
                return false;
            }
        }
        if (playlist != null) {
            playlist.put(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeToIfNotFound(String name, VideoData video) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            try {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = playlist.getJSONObject(i);
                    if (obj.getString(Constants.Json.Key.ID).equals(video.getId())) {
                        return false;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
            playlist.put(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeToIfNotFound(String name, VideoData video, int index) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            try {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = playlist.getJSONObject(i);
                    if (obj.getString(Constants.Json.Key.ID).equals(video.getId())) {
                        return false;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
            if (!insert(playlist, index, createVideo(video))) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeToOrReorder(String name, VideoData video, int index) {
        JSONObject root = read();
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            try {
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject obj = playlist.getJSONObject(i);
                    if (obj.getString(Constants.Json.Key.ID).equals(video.getId())) {
                        playlist.remove(i);
                        if (!insert(playlist, index, obj)) {
                            return false;
                        }
                        executeSave(root.toString());
                        return true;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean writeTo(String name, VideoData video, int index) {
        JSONObject root = read();
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
        JSONArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            playlist.put(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    private static void printInParts(String string) {
        int max = 1000;
        for (int i = 0; i <= string.length() / max; i++) {
            int start = i * max;
            int end = (i + 1) * max;
            end = end > string.length() ? string.length() : end;
            System.out.println(string.substring(start, end));
        }
    }

    public static JSONObject read() {
        try {
            return new JSONObject(doRead());
        } catch (JSONException | IOException e) {
            return new JSONObject();
        }
    }

    private static boolean write(JSONObject obj) {
        String json = obj.toString();
        executeSave(json);
        return true;
    }

    private static boolean doesFileExist() {
        String path = context.getFilesDir().getAbsolutePath() + "/" + Constants.Json.STORAGE_FILE_NAME;
        File file = new File(path);
        return file.exists();
    }

    public static boolean remove(String name) {
        JSONObject root = read();
        JSONArray playlists = getPlaylists(root);
        try {
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                if (playlist.getString(Constants.Json.Key.NAME).equals(name)) {
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
        JSONObject root = read();
        JSONArray playlists = getPlaylists(root);
        try {
            for (int i = 0; i < playlists.length(); i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                if (playlist.getString(Constants.Json.Key.NAME).equals(name)) {
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
