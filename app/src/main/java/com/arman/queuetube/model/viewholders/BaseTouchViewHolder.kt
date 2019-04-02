package com.arman.queuetube.model.viewholders

import android.graphics.Color
import android.view.DragEvent
import android.view.View

import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.util.itemtouchhelper.viewholders.ItemTouchHelperViewHolder

abstract class BaseTouchViewHolder<E>(itemView: View) : BaseViewHolder<E>(itemView), ItemTouchHelperViewHolder {

    open fun bind(item: E, clickListener: BaseTouchAdapter.OnItemClickListener?, dragListener: BaseTouchAdapter.OnItemDragListener?) {
        this.item = item
        this.itemView.setOnClickListener {
            clickListener?.onItemClick(this@BaseTouchViewHolder)
        }
        this.itemView.setOnDragListener(View.OnDragListener { view, dragEvent ->
            if (dragListener != null && DragEvent.ACTION_DRAG_STARTED == dragEvent.action) {
                dragListener.onItemDrag(this@BaseTouchViewHolder)
                return@OnDragListener true
            }
            false
        })
    }

    override fun onItemSelected() {
        this.itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemClear() {
        this.itemView.setBackgroundColor(Color.TRANSPARENT)
    }

}
