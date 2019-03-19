package com.arman.queuetube.model.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.arman.queuetube.R;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.VideoItemAdapter;

public class PlaylistItemViewHolder extends VideoViewHolder {

    public static final String TAG = "PlaylistItemViewHolder";

    public PlaylistItemViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(VideoData item, VideoItemAdapter.OnItemClickListener clickListener, VideoItemAdapter.OnItemDragListener dragListener) {
        super.bind(item, clickListener, dragListener);
    }

}
