package com.arman.queuetube.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arman.queuetube.R;
import com.arman.queuetube.activities.PlaylistActivity;
import com.arman.queuetube.model.adapters.BaseTouchAdapter;
import com.arman.queuetube.model.adapters.PlaylistsAdapter;
import com.arman.queuetube.modules.playlists.JSONPlaylistHelper;
import com.arman.queuetube.modules.playlists.LoadPlaylistsTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlaylistsFragment extends Fragment {

    private RecyclerView playlistsView;
    private PlaylistsAdapter playlistsAdapter;
    private SwipeRefreshLayout refreshLayout;
    private LoadPlaylistsTask loadPlaylistsTask;

    public void loadPlaylists() {
        AsyncTask.Status status = this.loadPlaylistsTask.getStatus();
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                this.loadPlaylistsTask = new LoadPlaylistsTask(this);
            }
            this.loadPlaylistsTask.execute();
        }
    }

    public PlaylistsAdapter getPlaylistsAdapter() {
        return playlistsAdapter;
    }

    public void finishRefresh() {
        this.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadPlaylists();
    }

    private void loadActivity(String name) {
        Intent intent = new Intent(getActivity(), PlaylistActivity.class);
        intent.putExtra("playlistName", name);
        this.startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.playlists_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout favoritesItem = (LinearLayout) view.findViewById(R.id.favorites_playlists_item);
        favoritesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadActivity(JSONPlaylistHelper.FAVORITES);
            }
        });

        LinearLayout historyItem = (LinearLayout) view.findViewById(R.id.history_playlists_item);
        historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadActivity(JSONPlaylistHelper.HISTORY);
            }
        });

        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.playlists_view_swipe_container);
        this.playlistsView = (RecyclerView) this.refreshLayout.findViewById(R.id.playlists_view);
        this.playlistsView.setHasFixedSize(true);

        this.playlistsAdapter = new PlaylistsAdapter(new BaseTouchAdapter.OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder) {
                String name = PlaylistsFragment.this.playlistsAdapter.get(viewHolder.getAdapterPosition());
                loadActivity(name);
            }
        });

        this.playlistsView.setAdapter(this.playlistsAdapter);

        this.loadPlaylistsTask = new LoadPlaylistsTask(this);

        Button createPlaylistButton = (Button) view.findViewById(R.id.playlists_create_playlist_button);
        createPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new CreatePlaylistFragment();
                dialog.show(getActivity().getSupportFragmentManager(), "create_playlist_dialog");
            }
        });

        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PlaylistsFragment.this.loadPlaylists();
            }
        });

    }

}
