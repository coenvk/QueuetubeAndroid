package com.arman.queuetube.model.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.viewholders.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<E, VH extends BaseViewHolder<E>> extends RecyclerView.Adapter<VH> {

    protected List<E> items;
    protected Context context;

    public BaseAdapter(Context context) {
        this(context, new ArrayList<E>());
    }

    public BaseAdapter(Context context, List<E> items) {
        this.context = context;
        this.items = items;
    }

    public E get(int index) {
        return this.items.get(index);
    }

    public boolean add(E item) {
        boolean res = this.items.add(item);
        if (res) {
            this.notifyItemInserted(this.items.size() - 1);
        }
        return res;
    }

    public boolean addAll(Collection<E> items) {
        int startRange = this.items.size();
        boolean res = this.items.addAll(items);
        if (res) {
            this.notifyItemRangeInserted(startRange, items.size());
        }
        return res;
    }

    public boolean remove(E item) {
        int index = this.items.indexOf(item);
        boolean res = this.items.remove(item);
        if (res) {
            this.notifyItemRemoved(index);
        }
        return res;
    }

    public E remove(int index) {
        E item = this.items.remove(index);
        if (item != null) {
            this.notifyItemRemoved(index);
        }
        return item;
    }

    public boolean removeAll(Collection<E> data) {
        boolean res = this.items.removeAll(data);
        this.notifyDataSetChanged();
        return res;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public E pop() {
        return this.remove(0);
    }

    public void clear() {
        this.items.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(this.items.get(position));
    }

    @Override
    public int getItemCount() {
        return this.size();
    }

    public boolean setAll(Collection<E> data) {
        this.items.clear();
        boolean ret = this.items.addAll(data);
        this.notifyDataSetChanged();
        return ret;
    }

    public int size() {
        return this.items.size();
    }

    public List<E> getAll() {
        return this.items;
    }

    public Context getContext() {
        return context;
    }

}
