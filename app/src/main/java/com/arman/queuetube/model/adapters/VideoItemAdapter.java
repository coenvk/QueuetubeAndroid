package com.arman.queuetube.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.viewholders.BaseTouchViewHolder;
import com.arman.queuetube.model.viewholders.VideoViewHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class VideoItemAdapter extends BaseTouchAdapter<VideoData, VideoViewHolder> {

    public static final String TAG = "VideoItemAdapter";

    public VideoItemAdapter(Context context) {
        super(context);
    }

    public VideoItemAdapter(Context context, List<VideoData> videoData, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, videoData, clickListener, dragListener);
    }

    public VideoItemAdapter(Context context, OnItemClickListener clickListener) {
        super(context, clickListener);
    }

    public VideoItemAdapter(Context context, OnItemDragListener dragListener) {
        super(context, dragListener);
    }

    public VideoItemAdapter(Context context, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, clickListener, dragListener);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

}
