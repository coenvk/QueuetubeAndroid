package com.arman.queuetube.model.adapters;

import android.content.Context;

import com.arman.queuetube.model.viewholders.BaseTouchViewHolder;
import com.arman.queuetube.util.itemtouchhelper.adapters.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseTouchAdapter<E, VH extends BaseTouchViewHolder<E>> extends BaseAdapter<E, VH> implements ItemTouchHelperAdapter {

    protected OnItemClickListener clickListener;
    protected OnItemDragListener dragListener;

    public BaseTouchAdapter(Context context) {
        super(context);
    }

    public BaseTouchAdapter(Context context, OnItemClickListener clickListener) {
        this(context, clickListener, null);
    }

    public BaseTouchAdapter(Context context, OnItemDragListener dragListener) {
        this(context, null, dragListener);
    }

    public BaseTouchAdapter(Context context, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context);
        this.clickListener = clickListener;
        this.dragListener = dragListener;
    }

    public BaseTouchAdapter(Context context, List<E> items, OnItemClickListener clickListener, OnItemDragListener dragListener) {
        super(context, items);
        this.clickListener = clickListener;
        this.dragListener = dragListener;
    }

    @Override
    public boolean onItemDragged(int fromIndex, int toIndex) {
        Collections.swap(this.items, fromIndex, toIndex);
        this.notifyItemMoved(fromIndex, toIndex);
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(this.items.get(position), this.clickListener, this.dragListener);
    }

    @Override
    public void onItemSwiped(int index) {

    }

    public interface OnItemClickListener {
        void onClick(RecyclerView.ViewHolder viewHolder);
    }

    public interface OnItemDragListener {
        void onDrag(RecyclerView.ViewHolder viewHolder);
    }

}
