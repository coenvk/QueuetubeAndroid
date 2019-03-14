package com.arman.queuetube.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.viewholders.PlaylistItemViewHolder;
import com.arman.queuetube.model.viewholders.VideoViewHolder;
import com.arman.queuetube.modules.playlists.PlaylistHelper;

import java.util.List;

public class PlaylistItemAdapter extends VideoItemAdapter {

    public PlaylistItemAdapter(Context context) {
        super(context);
    }

    public PlaylistItemAdapter(Context context, List<VideoData> videoData, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, videoData, clickListener, dragListener);
    }

    public PlaylistItemAdapter(Context context, OnItemClickListener clickListener) {
        super(context, clickListener);
    }

    public PlaylistItemAdapter(Context context, OnItemDragListener dragListener) {
        super(context, dragListener);
    }

    public PlaylistItemAdapter(Context context, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, clickListener, dragListener);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlist_item, parent, false);
        return new PlaylistItemViewHolder(this, view);
    }

    @Override
    public boolean onItemDragged(int fromIndex, int toIndex) {
        // TODO: save reordered playlist
//        PlaylistHelper.reorder(PlaylistHelper.FAVORITES, fromIndex, toIndex);
        return super.onItemDragged(fromIndex, toIndex);
    }

}
