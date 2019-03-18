package com.arman.queuetube.util.itemtouchhelper;

import android.graphics.Canvas;

import com.arman.queuetube.util.itemtouchhelper.adapters.ItemTouchHelperAdapter;
import com.arman.queuetube.util.itemtouchhelper.viewholders.ItemTouchHelperViewHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class VideoItemTouchHelper extends ItemTouchHelper {

    public VideoItemTouchHelper(@NonNull Callback callback) {
        super(callback);
    }

    public static class Callback extends ItemTouchHelper.Callback {

        private ItemTouchHelperAdapter adapter;

        private int dragFromIndex, dragToIndex;

        public Callback(ItemTouchHelperAdapter adapter) {
            this.adapter = adapter;
            this.dragFromIndex = -1;
            this.dragToIndex = -1;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
            return current.getItemViewType() == target.getItemViewType();
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }

            int fromIndex = viewHolder.getAdapterPosition();
            int toIndex = target.getAdapterPosition();

            if (this.dragFromIndex < 0) {
                this.dragFromIndex = fromIndex;
            }
            this.dragToIndex = toIndex;

            this.adapter.onItemDragged(viewHolder.getAdapterPosition(), target.getAdapterPosition(), false);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            this.adapter.onItemSwiped(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float alpha = 1f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
                }
            }

            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            if (this.dragFromIndex >= 0 && this.dragFromIndex != this.dragToIndex) {
                this.adapter.onItemDragged(this.dragFromIndex, this.dragToIndex, true);
            }
            this.dragFromIndex = this.dragToIndex = -1;

            viewHolder.itemView.setAlpha(1f);
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
            }
        }

    }

}
