package com.arman.queuetube.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.viewholders.VideoViewHolder;

import java.util.List;

public class VideoItemAdapter extends BaseTouchAdapter<VideoData, VideoViewHolder> {

    public static final String TAG = "VideoItemAdapter";

    public VideoItemAdapter() {
        super();
    }

    public VideoItemAdapter(List<VideoData> videoData, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(videoData, clickListener, dragListener);
    }

    public VideoItemAdapter(OnItemClickListener clickListener) {
        super(clickListener);
    }

    public VideoItemAdapter(OnItemDragListener dragListener) {
        super(dragListener);
    }

    public VideoItemAdapter(OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(clickListener, dragListener);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

}
