package com.arman.queuetube.util.itemtouchhelper.adapters;

public interface ItemTouchHelperAdapter {

    boolean onItemDragged(int fromIndex, int toIndex);

    void onItemSwiped(int index);

}
