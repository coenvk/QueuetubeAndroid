package com.arman.queuetube.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.R;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.viewholders.VideoViewHolder;
import com.arman.queuetube.util.itemtouchhelper.adapters.ItemTouchHelperAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoViewHolder> implements ItemTouchHelperAdapter {

    public static final String TAG = "VideoItemAdapter";

    private List<VideoData> videoData;
    private OnItemClickListener clickListener;
    private OnDragListener dragListener;

    public VideoItemAdapter(List<VideoData> videoData, OnItemClickListener clickListener, OnDragListener dragListener) {
        super();
        this.videoData = videoData;
        this.clickListener = clickListener;
        this.dragListener = dragListener;
    }

    public VideoItemAdapter(OnItemClickListener clickListener) {
        this(clickListener, null);
    }

    public VideoItemAdapter(OnDragListener dragListener) {
        this(null, dragListener);
    }

    public VideoItemAdapter(OnItemClickListener clickListener, OnDragListener dragListener) {
        this(new LinkedList<VideoData>(), clickListener, dragListener);
    }

    public VideoData get(int index) {
        return this.videoData.get(index);
    }

    public boolean add(VideoData data) {
        boolean res = this.videoData.add(data);
        if (res) {
            this.notifyItemInserted(this.videoData.size() - 1);
        }
        return res;
    }

    public boolean addAll(Collection<VideoData> data) {
        int startRange = this.videoData.size();
        boolean res = this.videoData.addAll(data);
        if (res) {
            this.notifyItemRangeInserted(startRange, data.size());
        }
        return res;
    }

    public boolean remove(VideoData data) {
        int index = this.videoData.indexOf(data);
        boolean res = this.videoData.remove(data);
        if (res) {
            this.notifyItemRemoved(index);
        }
        return res;
    }

    public VideoData remove(int index) {
        VideoData videoData = this.videoData.remove(index);
        if (videoData != null) {
            this.notifyItemRemoved(index);
        }
        return videoData;
    }

    public boolean removeAll(Collection<VideoData> data) {
        boolean res = this.videoData.removeAll(data);
        this.notifyDataSetChanged();
        return res;
    }

    public boolean isEmpty() {
        return this.videoData.isEmpty();
    }

    public VideoData pop() {
        return this.remove(0);
    }

    public void clear() {
        this.videoData.clear();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.bind(videoData.get(position), clickListener, dragListener);
    }

    @Override
    public int getItemCount() {
        return videoData.size();
    }

    public void setAll(Collection<VideoData> data) {
        this.videoData.clear();
        this.videoData.addAll(data);
        this.notifyDataSetChanged();
    }

    public int size() {
        return this.videoData.size();
    }

    @Override
    public boolean onItemDragged(int fromIndex, int toIndex) {
        Collections.swap(this.videoData, fromIndex, toIndex);
        this.notifyItemMoved(fromIndex, toIndex);
        return true;
    }

    @Override
    public void onItemSwiped(int index) {
//        this.videoData.remove(index);
//        this.notifyItemRemoved(index);
    }

    public interface OnItemClickListener {
        void onClick(RecyclerView.ViewHolder viewHolder);
    }

    public interface OnDragListener {
        void onDrag(RecyclerView.ViewHolder viewHolder);
    }

}
