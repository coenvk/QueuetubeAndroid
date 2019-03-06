package com.arman.queuetube.model.viewholders;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.util.itemtouchhelper.viewholders.ItemTouchHelperViewHolder;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import androidx.recyclerview.widget.RecyclerView;

public class VideoViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    public static final String TAG = "VideoViewHolder";

    public YouTubeThumbnailView thumbnailView;
    public TextView titleView;

    private boolean readyForLoadingYoutubeThumbnail;

    public VideoViewHolder(View view) {
        super(view);
        this.thumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);
        this.titleView = (TextView) view.findViewById(R.id.video_title);
        this.readyForLoadingYoutubeThumbnail = true;
    }

    public void bind(final VideoData item, final VideoItemAdapter.OnItemClickListener clickListener, final VideoItemAdapter.OnDragListener dragListener) {
        this.titleView.setText(item.getTitle());
        this.itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                if (dragListener != null && DragEvent.ACTION_DRAG_STARTED == dragEvent.getAction()) {
                    dragListener.onDrag(VideoViewHolder.this);
                    return true;
                }
                return false;
            }
        });
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(VideoViewHolder.this);
                }
            }
        });
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

    @Override
    public void onItemSelected() {
        this.itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        this.itemView.setBackgroundColor(Color.TRANSPARENT);
    }

}
