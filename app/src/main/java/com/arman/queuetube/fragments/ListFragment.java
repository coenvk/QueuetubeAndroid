package com.arman.queuetube.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arman.queuetube.R;
import com.arman.queuetube.model.adapters.VideoItemAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    private LinearLayout emptyTextLayout;

    private RecyclerView playlistView;
    private VideoItemAdapter playlistAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private VideoItemAdapter.OnItemClickListener onItemClickListener;
    private VideoItemAdapter.OnDragListener onItemDragListener;

    public void setOnItemDragListener(VideoItemAdapter.OnDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }

    public void setOnItemClickListener(VideoItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public VideoItemAdapter getPlaylistAdapter() {
        return playlistAdapter;
    }

    public void showEmptyText() {
        this.playlistView.setVisibility(View.GONE);
        this.emptyTextLayout.setVisibility(View.VISIBLE);
    }

    public void showPlaylist() {
        this.emptyTextLayout.setVisibility(View.GONE);
        this.playlistView.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.emptyTextLayout = (LinearLayout) view.findViewById(R.id.playlist_empty_text_layout);

        this.playlistView = (RecyclerView) view.findViewById(R.id.playlist_view);
        this.playlistView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.playlistView.setLayoutManager(this.layoutManager);

        if (this.onItemDragListener == null) {
            this.onItemDragListener = new VideoItemAdapter.OnDragListener() {
                @Override
                public void onDrag(RecyclerView.ViewHolder viewHolder) {
                }
            };
        }

        this.playlistAdapter = new VideoItemAdapter(this.onItemClickListener, this.onItemDragListener);
        this.playlistView.setAdapter(this.playlistAdapter);
    }

}
