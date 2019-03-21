package com.arman.queuetube.modules.playlists;

import android.content.Context;
import android.os.AsyncTask;

import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.VideoData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GsonPlaylistHelper {

    private static final JsonPrimitive PRIM_FAVORITES = new JsonPrimitive(Constants.Json.Playlist.FAVORITES);
    private static final JsonPrimitive PRIM_HISTORY = new JsonPrimitive(Constants.Json.Playlist.HISTORY);

    private static Context context;
    private static SavePlaylistsTask savePlaylistsTask;

    private static Gson gson;

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

    private static JsonObject doRead() throws IOException {
        FileInputStream fis = context.openFileInput(Constants.Json.STORAGE_FILE_NAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JsonObject root = gson.fromJson(sb.toString(), JsonElement.class).getAsJsonObject();
        br.close();
        return root;
    }

    public static void onCreate(Context context) {
        GsonPlaylistHelper.context = context;
        savePlaylistsTask = new SavePlaylistsTask((MainActivity) context);
        gson = new GsonBuilder().setPrettyPrinting().create();
        boolean fileExists = doesFileExist();
        if (!fileExists) {
            JsonObject root = new JsonObject();
            JsonArray playlists = new JsonArray();
            playlists.add(newPlaylist(GsonPlaylistHelper.PRIM_HISTORY));
            playlists.add(newPlaylist(GsonPlaylistHelper.PRIM_FAVORITES));

            root.add(Constants.Json.Key.PLAYLISTS, playlists);
            executeSave(root.toString());
        }
    }

    public static JsonArray getFavorites() {
        return getPlaylist(read(), GsonPlaylistHelper.PRIM_FAVORITES);
    }

    public static JsonArray getHistory() {
        return getPlaylist(read(), GsonPlaylistHelper.PRIM_HISTORY);
    }

    public static boolean isFavorited(VideoData video) {
        JsonArray favorites = getFavorites();
        for (int i = 0; i < favorites.size(); i++) {
            JsonObject obj = favorites.get(i).getAsJsonObject();
            String id = obj.get(Constants.Json.Key.ID).getAsString();
            if (id.equals(video.getId())) {
                return true;
            }
        }
        return false;
    }

    private static JsonObject newPlaylist(JsonPrimitive name) {
        JsonObject obj = new JsonObject();
        obj.add(Constants.Json.Key.NAME, name);
        obj.add(Constants.Json.Key.PLAYLIST, new JsonArray());
        return obj;
    }

    private static JsonObject newPlaylist(String name) {
        JsonObject obj = new JsonObject();
        obj.addProperty(Constants.Json.Key.NAME, name);
        obj.add(Constants.Json.Key.PLAYLIST, new JsonArray());
        return obj;
    }

    private static JsonObject createVideo(VideoData video) {
        return gson.toJsonTree(video).getAsJsonObject();
    }

    public static JsonArray getPlaylists() {
        return getPlaylists(read());
    }

    private static JsonArray getPlaylists(JsonObject root) {
        return root.get(Constants.Json.Key.PLAYLISTS).getAsJsonArray();
    }

    public static JsonArray getUserPlaylists() {
        JsonObject root = read();
        JsonArray playlists = root.get(Constants.Json.Key.PLAYLISTS).getAsJsonArray();
        JsonArray userPlaylists = new JsonArray();
        for (JsonElement playlist : playlists) {
            JsonObject obj = playlist.getAsJsonObject();
            JsonPrimitive name = obj.getAsJsonPrimitive(Constants.Json.Key.NAME);
            if (!name.equals(PRIM_FAVORITES) && !name.equals(PRIM_HISTORY)) {
                userPlaylists.add(playlist);
            }
        }
        return userPlaylists;
    }

    public static JsonArray getPlaylist(JsonPrimitive name) {
        JsonObject root = read();
        return getPlaylist(root, name);
    }

    public static JsonArray getPlaylist(String name) {
        return getPlaylist(new JsonPrimitive(name));
    }

    private static JsonArray getPlaylist(JsonObject root, JsonPrimitive name) {
        JsonArray playlists = getPlaylists(root);
        assert playlists != null;
        for (int i = 0; i < playlists.size(); i++) {
            JsonObject obj = playlists.get(i).getAsJsonObject();
            if (obj.getAsJsonPrimitive(Constants.Json.Key.NAME).equals(name)) {
                return obj.get(Constants.Json.Key.PLAYLIST).getAsJsonArray();
            }
        }
        return null;
    }

    private static JsonArray getPlaylist(JsonObject root, String name) {
        return getPlaylist(root, new JsonPrimitive(name));
    }

    public static boolean writeNewIfNotFound(JsonPrimitive name) {
        JsonObject root = read();
        JsonArray playlists = getPlaylists(root);

        for (int i = 0; i < playlists.size(); i++) {
            JsonObject playlist = playlists.get(i).getAsJsonObject();
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).equals(name)) {
                return false;
            }
        }

        JsonObject obj = newPlaylist(name);

        playlists.add(obj);
        executeSave(root.toString());
        return true;
    }

    public static boolean writeNewIfNotFound(String name) {
        return writeNewIfNotFound(new JsonPrimitive(name));
    }

    public static boolean writeNew(JsonPrimitive name) {
        JsonObject root = read();
        assert root != null;
        JsonArray playlists = getPlaylists(root);
        assert playlists != null;

        JsonObject obj = newPlaylist(name);

        playlists.add(obj);
        executeSave(root.toString());
        return true;
    }

    public static boolean writeNew(String name) {
        return writeNew(new JsonPrimitive(name));
    }

    private static boolean insert(JsonArray array, int pos, JsonObject obj) {
        array.add(new JsonObject());
        for (int i = array.size() - 1; i > pos; i--) {
            array.set(i, array.get(i - 1));
        }
        array.set(pos, obj);
        return true;
    }

    public static boolean reorder(JsonPrimitive name, int fromIndex, int toIndex) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            JsonObject video = playlist.remove(fromIndex).getAsJsonObject();
            if (!insert(playlist, toIndex, video)) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean reorder(String name, int fromIndex, int toIndex) {
        return reorder(new JsonPrimitive(name), fromIndex, toIndex);
    }

    public static boolean reorder(JsonPrimitive name, VideoData video, int toIndex) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            for (int i = 0; i < playlist.size(); i++) {
                JsonObject obj = playlist.get(i).getAsJsonObject();
                if (obj.get(Constants.Json.Key.ID).getAsString().equals(video.getId())) {
                    playlist.remove(i);
                    if (!insert(playlist, toIndex, obj)) {
                        return false;
                    }
                    executeSave(root.toString());
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean reorder(String name, VideoData video, int toIndex) {
        return reorder(new JsonPrimitive(name), video, toIndex);
    }

    public static boolean removeFrom(JsonPrimitive name, VideoData video) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            for (int i = 0; i < playlist.size(); i++) {
                JsonObject obj = playlist.get(i).getAsJsonObject();
                if (obj.get(Constants.Json.Key.ID).getAsString().equals(video.getId())) {
                    playlist.remove(i);
                    executeSave(root.toString());
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean removeFrom(String name, VideoData video) {
        return removeFrom(new JsonPrimitive(name), video);
    }

    public static boolean writeToNew(JsonPrimitive name, VideoData video) {
        JsonObject root = read();
        JsonArray playlists = getPlaylists(root);

        JsonArray playlist = getPlaylist(root, name);
        if (playlist == null) {
            JsonObject obj = newPlaylist(name);
            playlists.add(obj);
            playlist = obj.getAsJsonArray(Constants.Json.Key.PLAYLIST);
        }
        if (playlist != null) {
            playlist.add(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeToNew(String name, VideoData video) {
        return writeToNew(new JsonPrimitive(name), video);
    }

    public static boolean writeToIfNotFound(JsonPrimitive name, VideoData video) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            for (JsonElement jsonElement : playlist) {
                JsonObject obj = jsonElement.getAsJsonObject();
                if (obj.get(Constants.Json.Key.ID).getAsString().equals(video.getId())) {
                    return false;
                }
            }
            playlist.add(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeToIfNotFound(String name, VideoData video) {
        return writeToIfNotFound(new JsonPrimitive(name), video);
    }

    public static boolean writeToIfNotFound(JsonPrimitive name, VideoData video, int index) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            for (JsonElement jsonElement : playlist) {
                JsonObject obj = jsonElement.getAsJsonObject();
                if (obj.get(Constants.Json.Key.ID).getAsString().equals(video.getId())) {
                    return false;
                }
            }
            if (!insert(playlist, index, createVideo(video))) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeToIfNotFound(String name, VideoData video, int index) {
        return writeToIfNotFound(new JsonPrimitive(name), video, index);
    }

    public static boolean writeToOrReorder(JsonPrimitive name, VideoData video, int index) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            for (int i = 0; i < playlist.size(); i++) {
                JsonObject obj = playlist.get(i).getAsJsonObject();
                if (obj.get(Constants.Json.Key.ID).getAsString().equals(video.getId())) {
                    playlist.remove(i);
                    if (!insert(playlist, index, obj)) {
                        return false;
                    }
                    executeSave(root.toString());
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean writeToOrReorder(String name, VideoData video, int index) {
        return writeToOrReorder(new JsonPrimitive(name), video, index);
    }

    public static boolean writeTo(JsonPrimitive name, VideoData video, int index) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            if (!insert(playlist, index, createVideo(video))) {
                return false;
            }
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeTo(String name, VideoData video, int index) {
        return writeTo(new JsonPrimitive(name), video, index);
    }

    public static boolean writeTo(JsonPrimitive name, VideoData video) {
        JsonObject root = read();
        JsonArray playlist = getPlaylist(root, name);
        if (playlist != null) {
            playlist.add(createVideo(video));
            executeSave(root.toString());
            return true;
        }
        return false;
    }

    public static boolean writeTo(String name, VideoData video) {
        return writeTo(new JsonPrimitive(name), video);
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

    public static JsonObject read() {
        try {
            return doRead();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new JsonObject();
        }
    }

    private static boolean write(JsonObject obj) {
        String json = obj.toString();
        executeSave(json);
        return true;
    }

    private static boolean doesFileExist() {
        String path = context.getFilesDir().getAbsolutePath() + "/" + Constants.Json.STORAGE_FILE_NAME;
        File file = new File(path);
        return file.exists();
    }

    public static boolean remove(JsonPrimitive name) {
        JsonObject root = read();
        JsonArray playlists = getPlaylists(root);
        for (int i = 0; i < playlists.size(); i++) {
            JsonObject playlist = playlists.get(i).getAsJsonObject();
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).equals(name)) {
                playlists.remove(i);
                executeSave(root.toString());
                return true;
            }
        }
        return false;
    }

    public static boolean remove(String name) {
        return remove(new JsonPrimitive(name));
    }

    public static boolean clear(JsonPrimitive name) {
        JsonObject root = read();
        JsonArray playlists = getPlaylists(root);
        for (int i = 0; i < playlists.size(); i++) {
            JsonObject playlist = playlists.get(i).getAsJsonObject();
            if (playlist.getAsJsonPrimitive(Constants.Json.Key.NAME).equals(name)) {
                playlists.remove(i);
                playlists.add(newPlaylist(name));
                executeSave(root.toString());
                return true;
            }
        }
        return false;
    }

    public static boolean clear(String name) {
        return clear(new JsonPrimitive(name));
    }

}
