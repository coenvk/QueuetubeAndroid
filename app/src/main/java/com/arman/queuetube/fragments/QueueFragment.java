package com.arman.queuetube.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arman.queuetube.R;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueueFragment extends Fragment {

    private LinearLayout emptyTextLayout;

    private RecyclerView queueView;
    private VideoItemAdapter queueAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private VideoItemAdapter.OnItemClickListener onItemClickListener;
    private VideoItemAdapter.OnItemDragListener onItemDragListener;

    private ItemTouchHelper itemTouchHelper;

    public void setOnItemDragListener(VideoItemAdapter.OnItemDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }

    public void setOnItemClickListener(VideoItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public VideoItemAdapter getQueueAdapter() {
        return queueAdapter;
    }

    public void showEmptyText() {
        this.queueView.setVisibility(View.GONE);
        this.emptyTextLayout.setVisibility(View.VISIBLE);
    }

    public void showQueue() {
        this.emptyTextLayout.setVisibility(View.GONE);
        this.queueView.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.queue_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.emptyTextLayout = (LinearLayout) view.findViewById(R.id.queue_empty_text_layout);

        this.queueView = (RecyclerView) view.findViewById(R.id.playlist_view);
        this.queueView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.queueView.setLayoutManager(this.layoutManager);

        if (this.onItemDragListener == null) {
            this.onItemDragListener = new VideoItemAdapter.OnItemDragListener() {
                @Override
                public void onDrag(RecyclerView.ViewHolder viewHolder) {
                    QueueFragment.this.itemTouchHelper.startDrag(viewHolder);
                }
            };
        }

        this.queueAdapter = new VideoItemAdapter(this.onItemClickListener, this.onItemDragListener);
        this.queueView.setAdapter(this.queueAdapter);

        ItemTouchHelper.Callback callback = new VideoItemTouchHelper.Callback(this.queueAdapter);
        this.itemTouchHelper = new ItemTouchHelper(callback);
        this.itemTouchHelper.attachToRecyclerView(this.queueView);

    }

}
