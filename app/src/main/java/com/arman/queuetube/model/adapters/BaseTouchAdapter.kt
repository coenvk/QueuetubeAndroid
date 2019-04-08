package com.arman.queuetube.model.adapters

import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.model.viewholders.BaseTouchViewHolder
import com.arman.queuetube.util.itemtouchhelper.adapters.ItemTouchHelperAdapter
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseTouchAdapter<E, VH : BaseTouchViewHolder<E>> : BaseAdapter<E, VH>, ItemTouchHelperAdapter {

    var onItemClickListener: OnItemClickListener? = null
    var onItemDragListener: OnItemDragListener? = null

    constructor() : super()

    constructor(dragListener: OnItemDragListener?) : this(null, dragListener)

    @JvmOverloads
    constructor(clickListener: OnItemClickListener?, dragListener: OnItemDragListener? = null) : super() {
        this.onItemClickListener = clickListener
        this.onItemDragListener = dragListener
    }

    constructor(items: ArrayList<E>, clickListener: OnItemClickListener?, dragListener: OnItemDragListener?) : super(items) {
        this.onItemClickListener = clickListener
        this.onItemDragListener = dragListener
    }

    constructor(items: ArrayList<E>, clickListener: OnItemClickListener?) : super(items) {
        this.onItemClickListener = clickListener
    }

    override fun onItemDragged(fromIndex: Int, toIndex: Int, dragFinished: Boolean): Boolean {
        Collections.swap(this.items, fromIndex, toIndex)
        this.notifyItemMoved(fromIndex, toIndex)
        return true
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(this.items[position], this.onItemClickListener, this.onItemDragListener)
    }

    override fun onItemSwiped(index: Int) {

    }

    inline fun OnItemClickListener(
            crossinline onItemClick: (RecyclerView.ViewHolder) -> Unit = {}
    ): OnItemClickListener {
        return object : OnItemClickListener {
            override fun onItemClick(holder: RecyclerView.ViewHolder) {
                onItemClick(holder)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(holder: RecyclerView.ViewHolder)
    }

    inline fun OnItemDragListener(
            crossinline onItemDrag: (RecyclerView.ViewHolder) -> Unit = {}
    ): OnItemDragListener {
        return object : OnItemDragListener {
            override fun onItemDrag(holder: RecyclerView.ViewHolder) {
                onItemDrag(holder)
            }
        }
    }

    interface OnItemDragListener {
        fun onItemDrag(holder: RecyclerView.ViewHolder)
    }

}
