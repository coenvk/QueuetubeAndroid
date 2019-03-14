package com.arman.queuetube.model.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<E> extends RecyclerView.ViewHolder {

    protected E item;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(E item) {
        this.item = item;
    }

}
