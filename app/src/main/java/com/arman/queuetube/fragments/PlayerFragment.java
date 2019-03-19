package com.arman.queuetube.fragments;

import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.fragments.pager.ViewPagerAdapter;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.modules.playlists.GsonPlaylistHelper;
import com.arman.queuetube.modules.playlists.JSONPlaylistHelper;
import com.arman.queuetube.modules.search.SearchTask;
import com.arman.queuetube.modules.search.YouTubeSearcher;
import com.arman.queuetube.util.VideoSharer;
import com.arman.queuetube.util.notifications.NotificationHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class PlayerFragment extends Fragment implements YouTubePlayerInitListener {

    private SearchTask searchTask;
    private YouTubeSearcher ytSearcher;

    private LinearLayout fragmentLayout;
    private TextView videoTitleView;

    private ImageView favoriteButton;
    private ImageView addToPlaylistButton;
    private ImageView shareButton;

    private YouTubePlayerView ytPlayerView;
    private YouTubePlayer ytPlayer;
    private YouTubePlayerTracker ytPlayerTracker;
    private boolean ytPlayerReady;
    private boolean ytPlayerVideoSet;
    private boolean ytPlayerPlaying;
    private boolean ytPlayerStopped;

    private VideoData currentVideo;

    private QueueFragment queueFragment;
    private SearchFragment searchFragment;

    private NotificationHelper notificationHelper;

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public boolean setQueueTo(Collection<VideoData> videoData) {
        return this.getPlaylistAdapter().setAll(videoData);
    }

    public boolean addToQueue(Collection<VideoData> videoData) {
        return this.getPlaylistAdapter().addAll(videoData);
    }

    public boolean addToQueue(VideoData video) {
        return this.getPlaylistAdapter().add(video);
    }

    public void showAddToPlaylistDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AddToPlaylistFragment dialog = new AddToPlaylistFragment();
        dialog.setVideo(this.currentVideo);
        dialog.show(fragmentManager, "add_to_playlist_dialog");
//        if (getResources().getBoolean(R.bool.large_layout)) {
//            dialog.show(fragmentManager, "add_to_playlist_dialog");
//        } else {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.add(android.R.id.content, dialog).addToBackStack(null).commit();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.ytPlayerView.release();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.player_fragment, container, false);

        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).autoplay(1).modestBranding(1).ivLoadPolicy(3).rel(0).build();

        this.ytPlayerView = (YouTubePlayerView) rootView.findViewById(R.id.youtube_player);
        this.ytPlayerView.initialize(this, true, options);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.currentVideo = new VideoData();
        this.ytSearcher = new YouTubeSearcher();

        this.fragmentLayout = (LinearLayout) view.findViewById(R.id.player_fragment_layout);
        this.videoTitleView = (TextView) this.fragmentLayout.findViewById(R.id.video_title_text_view);

        this.favoriteButton = (ImageView) this.fragmentLayout.findViewById(R.id.favorite_button);
        this.addToPlaylistButton = (ImageView) this.fragmentLayout.findViewById(R.id.add_to_playlist_button);
        this.shareButton = (ImageView) this.fragmentLayout.findViewById(R.id.share_button);

        this.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment.this.favoriteVideo(!PlayerFragment.this.currentVideo.isFavorited());
            }
        });

        this.addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerFragment.this.showAddToPlaylistDialog();
            }
        });

        this.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoSharer.share(getContext(), PlayerFragment.this.currentVideo);
            }
        });

        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
        this.queueFragment = (QueueFragment) viewPagerAdapter.getFragmentByIndex(ViewPagerAdapter.QUEUE_INDEX);
        this.searchFragment = (SearchFragment) viewPagerAdapter.getFragmentByIndex(ViewPagerAdapter.SEARCH_INDEX);

        this.searchTask = new SearchTask(this.ytSearcher, this.searchFragment);

        this.notificationHelper = new NotificationHelper(getActivity());
    }

    public void updateVideo(boolean favorited) {
        this.currentVideo.setFavorited(favorited);
        this.adjustFavoriteButton(favorited);
    }

    private void favoriteVideo(boolean favorited) {
        if (favorited) {
            JSONPlaylistHelper.writeTo(JSONPlaylistHelper.FAVORITES, this.currentVideo);
        } else {
            JSONPlaylistHelper.removeFrom(JSONPlaylistHelper.FAVORITES, this.currentVideo);
        }

        this.updateVideo(favorited);
    }

    private void adjustFavoriteButton(boolean favorited) {
        if (favorited) {
            this.favoriteButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_star_black_36dp));
        } else {
            this.favoriteButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_star_outline_black_36dp));
        }
    }

    private boolean shouldSaveHistory() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getString(R.string.save_history_key), true);
    }

    private boolean isAutoplayEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getString(R.string.enable_autoplay_key), false);
    }

    public void query(String query) {
        AsyncTask.Status status = this.searchTask.getStatus();
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                this.searchTask = new SearchTask(this.ytSearcher, this.searchFragment);
            }
            this.searchTask.execute(query);
        }
    }

    private VideoItemAdapter getPlaylistAdapter() {
        return this.queueFragment.getQueueAdapter();
    }

    public boolean forcePlayNext() {
        boolean ret = false;
        if (this.ytPlayerReady && this.ytPlayerVideoSet) {
            this.skip();
            if (!this.getPlaylistAdapter().isEmpty()) {
                this.queueFragment.showQueue();
            }
        } else {
            ret = this.tryPlayNext();
        }
        return ret;
    }

    public boolean tryPlayNext() {
        return this.tryPlayNext(true);
    }

    public boolean tryPlayNext(boolean autoplayIfEnabled) {
        boolean ret = false;
        if (this.ytPlayerReady && !this.ytPlayerVideoSet) {
            if (!this.getPlaylistAdapter().isEmpty()) {
                VideoData nextVideo = this.getPlaylistAdapter().pop();
                this.currentVideo.setTo(nextVideo);
                this.ytPlayer.cueVideo(this.currentVideo.getId(), 0);
                ret = true;
            } else if (this.isAutoplayEnabled() && autoplayIfEnabled) {
                VideoData nextVideo = null;
                nextVideo = this.ytSearcher.nextAutoplay(this.currentVideo.getId());
                if (nextVideo != null) {
                    this.currentVideo.setTo(nextVideo);
                    this.ytPlayer.cueVideo(this.currentVideo.getId(), 0);
                    ret = true;
                }
            }
        }
        if (!this.getPlaylistAdapter().isEmpty()) {
            this.queueFragment.showQueue();
        }
        return ret;
    }

    private boolean doTryPlayNext() {
        return this.doTryPlayNext(true);
    }

    private boolean doTryPlayNext(boolean autoplayIfEnabled) {
        if (this.ytPlayerReady && !this.ytPlayerVideoSet) {
            if (!this.getPlaylistAdapter().isEmpty()) {
                VideoData nextVideo = this.getPlaylistAdapter().pop();
                this.currentVideo.setTo(nextVideo);
                this.ytPlayer.cueVideo(this.currentVideo.getId(), 0);
                return true;
            } else if (this.isAutoplayEnabled() && autoplayIfEnabled) {
                VideoData nextVideo = null;
                nextVideo = this.ytSearcher.nextAutoplay(this.currentVideo.getId());
                if (nextVideo != null) {
                    this.currentVideo.setTo(nextVideo);
                    this.ytPlayer.cueVideo(this.currentVideo.getId(), 0);
                    return true;
                }
            }
        }
        return false;
    }

    public void playNext() {
        if (!doTryPlayNext(!this.ytPlayerStopped)) {
            this.fragmentLayout.setVisibility(View.GONE);
            NotificationHelper.destroyNotification(getContext());
        }
    }

    public void stop() {
        this.stop(false);
    }

    public void stop(boolean autoplayIfEnabled) {
        this.queueFragment.getQueueAdapter().clear();
        this.ytPlayerStopped = !autoplayIfEnabled;
        this.ytPlayer.seekTo(this.ytPlayerTracker.getVideoDuration());
    }

    public void play() {
        this.ytPlayer.play();
    }

    public void pause() {
        this.ytPlayer.pause();
    }

    public void skip() {
        if (this.ytPlayer != null) {
            if (this.getPlaylistAdapter().isEmpty()) {
                this.stop(true);
            } else {
                onEnd();
            }
        }
    }

    private void onEnd() {
        if (this.shouldSaveHistory()) {
            JSONPlaylistHelper.writeToOrReorder(JSONPlaylistHelper.HISTORY, this.currentVideo, 0);
        }

        this.ytPlayerVideoSet = false;

        this.playNext();
        if (this.getPlaylistAdapter().isEmpty()) {
            this.queueFragment.showEmptyText();
        }

        this.ytPlayerStopped = false;
    }

    private void onPlaying() {
        this.notificationHelper.updateNotificationIfBuilt(this.currentVideo.getTitle(), true);
        this.ytPlayerPlaying = true;
    }

    private void onPaused() {
        this.notificationHelper.updateNotificationIfBuilt(this.currentVideo.getTitle(), false);
        this.ytPlayerPlaying = false;
    }

    private void onVideoCued() {
        this.currentVideo.setTo(this.ytSearcher.requestDetails(this.currentVideo));

        boolean favorited = JSONPlaylistHelper.isFavorited(this.currentVideo);
        this.currentVideo.setFavorited(favorited);
        this.adjustFavoriteButton(favorited);
        this.videoTitleView.setText(this.currentVideo.getTitle());
        if (this.fragmentLayout.getVisibility() == View.GONE) {
            this.fragmentLayout.setVisibility(View.VISIBLE);
        }
        this.ytPlayer.play();
        this.notificationHelper.updateNotificationIfBuilt(this.currentVideo.getTitle(), true);
        this.ytPlayerVideoSet = true;
    }

    @Override
    public void onInitSuccess(com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer youTubePlayer) {
        System.out.println("Init success");
        if (youTubePlayer != null) {
            System.out.println("Player initialized");
            this.ytPlayer = youTubePlayer;
            this.ytPlayerTracker = new YouTubePlayerTracker();
            this.ytPlayer.addListener(this.ytPlayerTracker);
            this.ytPlayer.addListener(new YouTubePlayerListener() {
                @Override
                public void onReady() {
                    PlayerFragment.this.ytPlayerView.enableBackgroundPlayback(true);

                    PlayerUIController uiController = PlayerFragment.this.ytPlayerView.getPlayerUIController();
                    uiController.setCustomAction2(getActivity().getDrawable(R.drawable.ic_skip_next_white_36dp), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PlayerFragment.this.skip();
                        }
                    });
                    PlayerFragment.this.ytPlayerReady = true;
                    System.out.println("The player is ready!");
                }

                @Override
                public void onStateChange(PlayerConstants.PlayerState state) {
                    switch (state) {
                        case UNKNOWN:
                            break;
                        case UNSTARTED:
                            break;
                        case ENDED:
                            onEnd();
                            break;
                        case PLAYING:
                            onPlaying();
                            break;
                        case PAUSED:
                            onPaused();
                            break;
                        case BUFFERING:
                            break;
                        case VIDEO_CUED:
                            onVideoCued();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onPlaybackQualityChange(PlayerConstants.PlaybackQuality playbackQuality) {

                }

                @Override
                public void onPlaybackRateChange(PlayerConstants.PlaybackRate playbackRate) {

                }

                @Override
                public void onError(PlayerConstants.PlayerError error) {
                    PlayerFragment.this.skip();
                }

                @Override
                public void onApiChange() {

                }

                @Override
                public void onCurrentSecond(float second) {

                }

                @Override
                public void onVideoDuration(float duration) {

                }

                @Override
                public void onVideoLoadedFraction(float loadedFraction) {

                }

                @Override
                public void onVideoId(String videoId) {

                }
            });
        }
    }

    public VideoData getCurrentVideo() {
        return currentVideo;
    }

}
