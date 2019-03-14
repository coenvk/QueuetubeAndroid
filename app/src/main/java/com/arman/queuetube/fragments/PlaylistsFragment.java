package com.arman.queuetube.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.adapters.BaseTouchAdapter;
import com.arman.queuetube.model.adapters.PlaylistsAdapter;
import com.arman.queuetube.modules.playlists.LoadPlaylistsTask;
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.playlists_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.playlists_view_swipe_container);
        this.playlistsView = (RecyclerView) this.refreshLayout.findViewById(R.id.playlists_view);
        this.playlistsView.setHasFixedSize(true);

        this.playlistsAdapter = new PlaylistsAdapter(getActivity(), new BaseTouchAdapter.OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder) {
                // TODO: open playlist
            }
        });

        this.playlistsView.setAdapter(this.playlistsAdapter);

        ItemTouchHelper.Callback callback = new VideoItemTouchHelper.Callback(this.playlistsAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(this.playlistsView);

        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PlaylistsFragment.this.loadPlaylists();
            }
        });

    }

}
