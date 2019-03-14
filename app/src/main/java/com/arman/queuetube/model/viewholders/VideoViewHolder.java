package com.arman.queuetube.model.viewholders;

import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class VideoViewHolder extends BaseTouchViewHolder<VideoData> {

    public static final String TAG = "VideoViewHolder";

    private YouTubeThumbnailView thumbnailView;
    private TextView titleView;

    private boolean readyForLoadingYoutubeThumbnail;

    public VideoViewHolder(View view) {
        super(view);
        this.thumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);
        this.titleView = (TextView) view.findViewById(R.id.video_title);
        this.readyForLoadingYoutubeThumbnail = true;
    }

    public void bind(final VideoData item, final VideoItemAdapter.OnItemClickListener clickListener, final VideoItemAdapter.OnItemDragListener dragListener) {
        super.bind(item, clickListener, dragListener);
        this.titleView.setText(item.getTitle());
        if (this.readyForLoadingYoutubeThumbnail) {
            this.readyForLoadingYoutubeThumbnail = false;
            this.thumbnailView.initialize(Constants.Key.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(item.getId());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailView.setVisibility(View.VISIBLE);
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            youTubeThumbnailLoader.release();
                        }
                    });
                    VideoViewHolder.this.readyForLoadingYoutubeThumbnail = true;
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        }
    }

}
