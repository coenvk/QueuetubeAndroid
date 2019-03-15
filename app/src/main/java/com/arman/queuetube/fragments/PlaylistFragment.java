package com.arman.queuetube.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.model.adapters.PlaylistItemAdapter;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.modules.playlists.LoadPlaylistTask;
import com.arman.queuetube.modules.playlists.PlaylistHelper;
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlaylistFragment extends Fragment {

    public static final String TAG = "PlaylistFragment";

    private String playlistName;

    private TextView headerTitleView;
    private TextView headerSubtitleView;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView playlistView;
    private VideoItemAdapter playlistAdapter;

    private ItemTouchHelper itemTouchHelper;
    private LoadPlaylistTask loadPlaylistTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.playlist_fragment, container, false);
        return rootView;
    }

    public void setTitle(String title) {
        this.playlistName = title;
        this.headerTitleView.setText(this.playlistName);
    }

    public void setSubtitle(String subtitle) {
        this.headerSubtitleView.setText(subtitle);
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public VideoItemAdapter getPlaylistAdapter() {
        return playlistAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.playlistName = getArguments().getString("playlistName");
        }
    }

    public void loadPlaylist() {
        if (this.playlistName != null) {
            AsyncTask.Status status = this.loadPlaylistTask.getStatus();
            if (status != AsyncTask.Status.RUNNING) {
                if (status == AsyncTask.Status.FINISHED) {
                    this.loadPlaylistTask = new LoadPlaylistTask(this);
                }
                this.loadPlaylistTask.execute(this.playlistName);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadPlaylist();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.headerTitleView = (TextView) view.findViewById(R.id.playlist_header_title);
        this.headerTitleView.setText(this.playlistName);

        this.headerSubtitleView = (TextView) view.findViewById(R.id.playlist_header_subtitle);

        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.playlist_view_swipe_container);
        this.playlistView = (RecyclerView) this.refreshLayout.findViewById(R.id.playlist_view);
        this.playlistView.setHasFixedSize(true);

        this.playlistAdapter = new PlaylistItemAdapter(getActivity(), this.playlistName, new VideoItemAdapter.OnItemDragListener() {
            @Override
            public void onDrag(RecyclerView.ViewHolder viewHolder) {
                PlaylistFragment.this.itemTouchHelper.startDrag(viewHolder);
            }
        });
        this.playlistView.setAdapter(this.playlistAdapter);

        this.loadPlaylistTask = new LoadPlaylistTask(this);

        ItemTouchHelper.Callback callback = new VideoItemTouchHelper.Callback(this.playlistAdapter);
        this.itemTouchHelper = new ItemTouchHelper(callback);
        this.itemTouchHelper.attachToRecyclerView(this.playlistView);

        FloatingActionButton playAllButton = (FloatingActionButton) view.findViewById(R.id.playlist_play_all_button);
        playAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setQueueTo(PlaylistFragment.this.playlistAdapter.getAll());
            }
        });

        ImageView clearButton = (ImageView) view.findViewById(R.id.playlist_clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistHelper.clear(PlaylistFragment.this.playlistName);
            }
        });

        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PlaylistFragment.this.loadPlaylist();
            }
        });

    }

    public void finishRefresh() {
        this.refreshLayout.setRefreshing(false);
        int videoCount = this.playlistAdapter.getItemCount();
        String subtitle = String.valueOf(videoCount) + " video's";
        this.headerSubtitleView.setText(subtitle);
    }

}
