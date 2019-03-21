package com.arman.queuetube.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.PlaylistItemAdapter;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.model.viewholders.BaseViewHolder;
import com.arman.queuetube.modules.playlists.GsonPlaylistHelper;
import com.arman.queuetube.modules.playlists.JSONPlaylistHelper;
import com.arman.queuetube.modules.playlists.LoadPlaylistTask;
import com.arman.queuetube.util.VideoSharer;
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collection;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlaylistFragment extends Fragment implements PlaylistItemAdapter.OnShowPopupMenuListener {

    public static final String TAG = "PlaylistFragment";

    private String playlistName;
    private boolean isClickable;
    private boolean isSortable;

    private TextView headerTitleView;
    private TextView headerSubtitleView;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView playlistView;
    private VideoItemAdapter playlistAdapter;

    private LoadPlaylistTask loadPlaylistTask;

    private FloatingActionButton playAllButton;

    private ItemTouchHelper itemTouchHelper;

    private OnPlayItemsListener onPlayItemsListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.playlist_fragment, container, false);
        return rootView;
    }

    public void setOnPlayItemsListener(OnPlayItemsListener onPlayItemsListener) {
        this.onPlayItemsListener = onPlayItemsListener;
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.playlistName = arguments.getString(Constants.Fragment.Argument.PLAYLIST_NAME);
            this.isClickable = arguments.getBoolean(Constants.Fragment.Argument.IS_CLICKABLE, true);
            this.isSortable = arguments.getBoolean(Constants.Fragment.Argument.IS_SORTABLE, true);
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

        this.playlistAdapter = new PlaylistItemAdapter(this.playlistName, this);
        this.playlistView.setAdapter(this.playlistAdapter);

        this.loadPlaylistTask = new LoadPlaylistTask(this);

        this.playAllButton = (FloatingActionButton) view.findViewById(R.id.playlist_play_all_button);
        this.playAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PlaylistFragment.this.onPlayItemsListener != null) {
                    PlaylistFragment.this.onPlayItemsListener.onPlayAll(PlaylistFragment.this.playlistAdapter.getAll());
                }
            }
        });

        ImageView clearButton = (ImageView) view.findViewById(R.id.playlist_clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GsonPlaylistHelper.clear(PlaylistFragment.this.playlistName);
            }
        });

        if (this.isClickable) {
            ItemTouchHelper.Callback callback = new VideoItemTouchHelper.Callback(this.playlistAdapter);
            this.itemTouchHelper = new ItemTouchHelper(callback);
            this.itemTouchHelper.attachToRecyclerView(this.playlistView);
        }

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
        String subtitle = String.valueOf(videoCount) + " videos";
        this.headerSubtitleView.setText(subtitle);
        if (videoCount <= 0) {
            this.playAllButton.setVisibility(View.GONE);
        } else {
            this.playAllButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShowPopupMenu(final BaseViewHolder<VideoData> holder, final View anchorView) {
        final Context context = anchorView.getContext();
        PopupMenu optionsPopup = new PopupMenu(context, anchorView);
        optionsPopup.getMenuInflater().inflate(R.menu.playlist_item_popup_menu, optionsPopup.getMenu());
        optionsPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                VideoData item = holder.getItem();
                switch (menuItem.getItemId()) {
                    case R.id.playlist_item_option_add_to_queue:
                        onPlayItemsListener.onPlay(item);
                        break;
                    case R.id.playlist_item_option_remove:
                        playlistAdapter.remove(item);
                        GsonPlaylistHelper.removeFrom(playlistName, item);
                        break;
                    case R.id.playlist_item_option_share:
                        VideoSharer.share(context, item);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        optionsPopup.show();
    }

    public interface OnPlayItemsListener {

        public void onPlayAll(Collection<VideoData> videos);

        public void onPlay(VideoData video);

    }

}
