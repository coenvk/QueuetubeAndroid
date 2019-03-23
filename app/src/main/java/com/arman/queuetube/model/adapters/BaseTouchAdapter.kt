package com.arman.queuetube.model.adapters

import com.arman.queuetube.model.viewholders.BaseTouchViewHolder
import com.arman.queuetube.util.itemtouchhelper.adapters.ItemTouchHelperAdapter

import java.util.Collections
import androidx.recyclerview.widget.RecyclerView

abstract class BaseTouchAdapter<E, VH : BaseTouchViewHolder<E>> : BaseAdapter<E, VH>, ItemTouchHelperAdapter {

    protected var clickListener: OnItemClickListener? = null
    protected var dragListener: OnItemDragListener? = null

    constructor() : super() {}

    constructor(dragListener: OnItemDragListener) : this(null, dragListener) {}

    @JvmOverloads
    constructor(clickListener: OnItemClickListener?, dragListener: OnItemDragListener? = null) : super() {
        this.clickListener = clickListener
        this.dragListener = dragListener
    }

    constructor(items: MutableList<E>, clickListener: OnItemClickListener, dragListener: OnItemDragListener) : super(items) {
        this.clickListener = clickListener
        this.dragListener = dragListener
    }

    override fun onItemDragged(fromIndex: Int, toIndex: Int, dragFinished: Boolean): Boolean {
        Collections.swap(this.items, fromIndex, toIndex)
        this.notifyItemMoved(fromIndex, toIndex)
        return true
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(this.items[position], this.clickListener, this.dragListener)
    }

    override fun onItemSwiped(index: Int) {

    }

    interface OnItemClickListener {
        fun onClick(viewHolder: RecyclerView.ViewHolder)
    }

    interface OnItemDragListener {
        fun onDrag(viewHolder: RecyclerView.ViewHolder)
    }

}
