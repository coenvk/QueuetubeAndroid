package com.arman.queuetube.fragments;

import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.fragments.pager.ViewPagerAdapter;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.modules.search.YouTubeSearcher;
import com.arman.queuetube.util.notifications.NotificationHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class PlayerFragment extends Fragment implements YouTubePlayerInitListener {

    private YouTubeSearcher ytSearcher;

    private LinearLayout fragmentLayout;
    private TextView videoTitleView;

    private YouTubePlayerView ytPlayerView;
    private YouTubePlayer ytPlayer;
    private YouTubePlayerTracker ytPlayerTracker;
    private boolean ytPlayerReady;
    private boolean ytPlayerVideoSet;
    private boolean ytPlayerPlaying;

    private VideoData currentVideo;

    private PlaylistFragment playlistFragment;
    private SearchFragment searchFragment;

    private NotificationHelper notificationHelper;

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
        this.playlistFragment = (PlaylistFragment) viewPagerAdapter.getFragmentByIndex(ViewPagerAdapter.PLAYLIST_INDEX);
        this.searchFragment = (SearchFragment) viewPagerAdapter.getFragmentByIndex(ViewPagerAdapter.SEARCH_INDEX);

        this.notificationHelper = new NotificationHelper(getActivity());
    }

    private boolean autoplayEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getContext().getString(R.string.enable_autoplay_key), false);
    }

    public void query(String query) {
        List<VideoData> searchResults = this.ytSearcher.search(query);
        this.searchFragment.getResultsAdapter().setAll(searchResults);
        if (!searchResults.isEmpty()) {
            this.searchFragment.showResults();
            this.searchFragment.scrollToTop();
        } else {
            this.searchFragment.showEmptyText();
        }
    }

    private VideoItemAdapter getPlaylistAdapter() {
        return this.playlistFragment.getPlaylistAdapter();
    }

    public boolean tryPlayNext() {
        if (this.ytPlayerReady && !this.ytPlayerVideoSet) {
            if (!this.getPlaylistAdapter().isEmpty()) {
                this.currentVideo.setTo(this.getPlaylistAdapter().pop());
                this.ytPlayer.cueVideo(this.currentVideo.getId(), 0);
                return true;
            } else if (this.autoplayEnabled()) {
                VideoData nextVideo = this.ytSearcher.nextAutoplay(this.currentVideo.getId());
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
        if (!tryPlayNext()) {
            this.fragmentLayout.setVisibility(View.GONE);
            NotificationHelper.destroyNotification(getContext());
        }
    }

    public void stop() {
        this.playlistFragment.getPlaylistAdapter().clear();
        this.playlistFragment.showEmptyText();
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
                this.stop();
            } else {
                onEnd();
            }
        }
    }

    private void onEnd() {
        this.ytPlayerVideoSet = false;
        this.playNext();
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
        this.videoTitleView.setText(this.currentVideo.getTitle());
        if (this.fragmentLayout.getVisibility() == View.GONE) {
            this.fragmentLayout.setVisibility(View.VISIBLE);
        }
        ytPlayer.play();
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
                    System.out.println(error);
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

}
