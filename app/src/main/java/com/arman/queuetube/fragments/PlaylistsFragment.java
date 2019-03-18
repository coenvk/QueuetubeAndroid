package com.arman.queuetube.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.model.adapters.BaseTouchAdapter;
import com.arman.queuetube.model.adapters.PlaylistsAdapter;
import com.arman.queuetube.modules.playlists.LoadPlaylistsTask;
import com.arman.queuetube.modules.playlists.PlaylistHelper;
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlaylistsFragment extends Fragment {

    private LinearLayout favoritesItem, historyItem;

    private TextView noPlaylistsText;

    private DefaultPlaylistFragment playlistFragment;

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
        if (this.playlistsAdapter.isEmpty()) {
            this.playlistsView.setVisibility(View.GONE);
            this.noPlaylistsText.setVisibility(View.VISIBLE);
        } else {
            this.noPlaylistsText.setVisibility(View.GONE);
            this.playlistsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadPlaylists();
    }

    private void setupFragment(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("playlistName", name);
        this.playlistFragment = new PlaylistFragment();
        this.playlistFragment.setArguments(bundle);
    }

    private void loadFragment(String name) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        this.setupFragment(name);
        transaction.replace(R.id.playlist_fragment_frame, this.playlistFragment);
        transaction.hide(this);
        transaction.addToBackStack(null).commit();
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

        this.favoritesItem = (LinearLayout) view.findViewById(R.id.favorites_playlists_item);
        this.favoritesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: open favorites playlist
                loadFragment(PlaylistHelper.FAVORITES);
            }
        });

        this.historyItem = (LinearLayout) view.findViewById(R.id.history_playlists_item);
        this.historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: open history playlist
                loadFragment(PlaylistHelper.HISTORY);
            }
        });

        this.noPlaylistsText = (TextView) view.findViewById(R.id.no_playlists_text);

        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.playlists_view_swipe_container);
        this.playlistsView = (RecyclerView) this.refreshLayout.findViewById(R.id.playlists_view);
        this.playlistsView.setHasFixedSize(true);

        this.playlistsAdapter = new PlaylistsAdapter(getActivity(), new BaseTouchAdapter.OnItemClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder viewHolder) {
                // TODO: open playlist
                String name = PlaylistsFragment.this.playlistsAdapter.get(viewHolder.getAdapterPosition());
                loadFragment(name);
            }
        });

        this.playlistsView.setAdapter(this.playlistsAdapter);

        ItemTouchHelper.Callback callback = new VideoItemTouchHelper.Callback(this.playlistsAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(this.playlistsView);

        this.loadPlaylistsTask = new LoadPlaylistsTask(this);

        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PlaylistsFragment.this.loadPlaylists();
            }
        });

    }

}
