package com.arman.queuetube.util.itemtouchhelper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.util.itemtouchhelper.adapters.ItemTouchHelperAdapter
import com.arman.queuetube.util.itemtouchhelper.viewholders.ItemTouchHelperViewHolder

class VideoItemTouchHelper(callback: Callback) : ItemTouchHelper(callback) {

    class Callback(private val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

        private var dragFromIndex: Int = 0
        private var dragToIndex: Int = 0

        init {
            this.dragFromIndex = -1
            this.dragToIndex = -1
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return false
        }

        override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return current.itemViewType == target.itemViewType
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (viewHolder.itemViewType != target.itemViewType) {
                return false
            }

            val fromIndex = viewHolder.adapterPosition
            val toIndex = target.adapterPosition

            if (this.dragFromIndex < 0) {
                this.dragFromIndex = fromIndex
            }
            this.dragToIndex = toIndex

            this.adapter.onItemDragged(viewHolder.adapterPosition, target.adapterPosition, false)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            this.adapter.onItemSwiped(viewHolder.adapterPosition)
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                val alpha = 1f - Math.abs(dX) / viewHolder.itemView.width.toFloat()
                viewHolder.itemView.alpha = alpha
                viewHolder.itemView.translationX = dX
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder is ItemTouchHelperViewHolder) {
                    (viewHolder as ItemTouchHelperViewHolder).onItemSelected()
                }
            }

            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            if (this.dragFromIndex >= 0 && this.dragFromIndex != this.dragToIndex) {
                this.adapter.onItemDragged(this.dragFromIndex, this.dragToIndex, true)
            }
            this.dragToIndex = -1
            this.dragFromIndex = this.dragToIndex

            viewHolder.itemView.alpha = 1f
            if (viewHolder is ItemTouchHelperViewHolder) viewHolder.onItemClear()
        }

    }

}
