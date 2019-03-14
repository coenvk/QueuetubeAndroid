package com.arman.queuetube.model.viewholders;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;

import com.arman.queuetube.model.adapters.BaseTouchAdapter;
import com.arman.queuetube.util.itemtouchhelper.viewholders.ItemTouchHelperViewHolder;

import androidx.annotation.NonNull;

public abstract class BaseTouchViewHolder<E> extends BaseViewHolder<E> implements ItemTouchHelperViewHolder {

    public BaseTouchViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final E item, final BaseTouchAdapter.OnItemClickListener clickListener, final BaseTouchAdapter.OnItemDragListener dragListener) {
        this.item = item;
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(BaseTouchViewHolder.this);
                }
            }
        });
        this.itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                if (dragListener != null && DragEvent.ACTION_DRAG_STARTED == dragEvent.getAction()) {
                    dragListener.onDrag(BaseTouchViewHolder.this);
                    return true;
                }
                return false;
            }
        });
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
