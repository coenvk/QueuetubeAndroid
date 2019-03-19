package com.arman.queuetube.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.viewholders.BaseViewHolder;
import com.arman.queuetube.model.viewholders.PlaylistItemViewHolder;
import com.arman.queuetube.model.viewholders.VideoViewHolder;
import com.arman.queuetube.modules.playlists.JSONPlaylistHelper;

import androidx.annotation.NonNull;

public class PlaylistItemAdapter extends VideoItemAdapter {

    private String playlistName;
    private OnShowPopupMenuListener onShowPopupMenuListener;

    public PlaylistItemAdapter(String playlistName, OnShowPopupMenuListener onShowPopupMenuListener) {
        this(playlistName);
        this.onShowPopupMenuListener = onShowPopupMenuListener;
    }

    public PlaylistItemAdapter(String playlistName, OnItemDragListener dragListener) {
        super(dragListener);
        this.playlistName = playlistName;
    }

    public PlaylistItemAdapter(String playlistName, OnItemClickListener clickListener) {
        super(clickListener);
        this.playlistName = playlistName;
    }

    public PlaylistItemAdapter() {
        super();
    }

    public PlaylistItemAdapter(String playlistName) {
        super();
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item, parent, false);
        return new PlaylistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        View optionsButton = holder.itemView.findViewById(R.id.playlist_item_options_button);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopupMenuListener.onShowPopupMenu(holder, view);
            }
        });
    }

    @Override
    public boolean onItemDragged(int fromIndex, int toIndex, boolean dragFinished) {
        if (dragFinished) {
            JSONPlaylistHelper.reorder(this.playlistName, fromIndex, toIndex);
            return true;
        } else {
            return super.onItemDragged(fromIndex, toIndex, false);
        }
    }

    public interface OnShowPopupMenuListener {

        public void onShowPopupMenu(BaseViewHolder<VideoData> holder, View anchorView);

    }

}
