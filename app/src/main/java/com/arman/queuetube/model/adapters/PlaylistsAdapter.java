package com.arman.queuetube.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.viewholders.PlaylistsViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

public class PlaylistsAdapter extends BaseTouchAdapter<String, PlaylistsViewHolder> {

    public static final String TAG = "PlaylistsAdapter";

    private List<String> playlists;
    private VideoItemAdapter.OnItemClickListener clickListener;

    public PlaylistsAdapter(Context context) {
        super(context);
    }

    public PlaylistsAdapter(Context context, OnItemClickListener clickListener) {
        super(context, clickListener);
    }

    public PlaylistsAdapter(Context context, OnItemDragListener dragListener) {
        super(context, dragListener);
    }

    public PlaylistsAdapter(Context context, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, clickListener, dragListener);
    }

    public PlaylistsAdapter(Context context, List<String> items, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, items, clickListener, dragListener);
    }

    @NonNull
    @Override
    public PlaylistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlists_item, parent, false);
        return new PlaylistsViewHolder(view);
    }

}
