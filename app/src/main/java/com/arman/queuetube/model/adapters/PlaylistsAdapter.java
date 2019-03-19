package com.arman.queuetube.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.model.viewholders.PlaylistsViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

public class PlaylistsAdapter extends BaseTouchAdapter<String, PlaylistsViewHolder> {

    public static final String TAG = "PlaylistsAdapter";

    public PlaylistsAdapter() {
        super();
    }

    public PlaylistsAdapter(OnItemClickListener clickListener) {
        super(clickListener);
    }

    public PlaylistsAdapter(OnItemDragListener dragListener) {
        super(dragListener);
    }

    public PlaylistsAdapter(OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(clickListener, dragListener);
    }

    public PlaylistsAdapter(List<String> items, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(items, clickListener, dragListener);
    }

    @NonNull
    @Override
    public PlaylistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playlists_item, parent, false);
        return new PlaylistsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TextView textView = (TextView) holder.itemView.findViewById(R.id.playlists_item_title);
        textView.setText(this.items.get(position));
    }

}
