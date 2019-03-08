package com.arman.queuetube.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.model.adapters.VideoItemAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    private LinearLayout innerLayout;

    private List<RecyclerView> playlistViews;
    private List<VideoItemAdapter> playlistAdapters;

    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_fragment, container, false);
        this.inflater = inflater;
        return rootView;
    }

    private void createNewPlaylistView(String name) {
        View view = inflater.inflate(R.layout.playlist, null, false);
        TextView textView = view.findViewById(R.id.playlist_header);
        textView.setText(name);
        RecyclerView recyclerView = view.findViewById(R.id.playlist_view);
        recyclerView.setHasFixedSize(true);
        VideoItemAdapter itemAdapter = new VideoItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        this.playlistAdapters.add(itemAdapter);
        this.playlistViews.add(recyclerView);
        this.innerLayout.addView(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.innerLayout = (LinearLayout) view.findViewById(R.id.playlist_inner_layout);

        this.playlistViews = new ArrayList<>();
        this.playlistAdapters = new ArrayList<>();

        this.createNewPlaylistView("Favorites");
        this.createNewPlaylistView("History");
    }

}
