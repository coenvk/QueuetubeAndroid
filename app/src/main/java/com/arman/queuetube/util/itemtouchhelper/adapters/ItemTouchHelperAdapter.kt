package com.arman.queuetube.util.itemtouchhelper.adapters

interface ItemTouchHelperAdapter {

    fun onItemDragged(fromIndex: Int, toIndex: Int, dragFinished: Boolean): Boolean

    fun onItemSwiped(index: Int)

}
