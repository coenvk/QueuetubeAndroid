package com.arman.queuetube.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.viewholders.PlaylistItemViewHolder;
import com.arman.queuetube.model.viewholders.VideoViewHolder;
import com.arman.queuetube.modules.playlists.PlaylistHelper;

public class PlaylistItemAdapter extends VideoItemAdapter {

    private String playlistName;

    public PlaylistItemAdapter(Context context, String playlistName, OnItemDragListener dragListener) {
        super(context, dragListener);
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item, parent, false);
        return new PlaylistItemViewHolder(this, view);
    }

    @Override
    public boolean onItemDragged(int fromIndex, int toIndex, boolean dragFinished) {
        if (dragFinished) {
            PlaylistHelper.reorder(this.playlistName, fromIndex, toIndex);
        }
        return super.onItemDragged(fromIndex, toIndex, dragFinished);
    }

}
