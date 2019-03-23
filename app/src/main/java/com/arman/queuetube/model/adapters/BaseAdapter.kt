package com.arman.queuetube.model.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.model.viewholders.BaseViewHolder
import java.util.*

abstract class BaseAdapter<E, VH : BaseViewHolder<E>> @JvmOverloads constructor(protected var items: MutableList<E> = ArrayList()) : RecyclerView.Adapter<VH>() {

    val isEmpty: Boolean
        get() = this.items.isEmpty()

    val all: List<E>
        get() = this.items

    operator fun get(index: Int): E {
        return this.items[index]
    }

    fun add(item: E): Boolean {
        val res = this.items.add(item)
        if (res) {
            this.notifyItemInserted(this.items.size - 1)
        }
        return res
    }

    fun addAll(items: Collection<E>): Boolean {
        val startRange = this.items.size
        val res = this.items.addAll(items)
        if (res) {
            this.notifyItemRangeInserted(startRange, items.size)
        }
        return res
    }

    fun remove(item: E): Boolean {
        val index = this.items.indexOf(item)
        val res = this.items.remove(item)
        if (res) {
            this.notifyItemRemoved(index)
        }
        return res
    }

    fun remove(index: Int): E? {
        val item = this.items.removeAt(index)
        if (item != null) {
            this.notifyItemRemoved(index)
        }
        return item
    }

    fun removeAll(data: Collection<E>): Boolean {
        val res = this.items.removeAll(data)
        this.notifyDataSetChanged()
        return res
    }

    fun pop(): E? {
        return this.remove(0)
    }

    fun clear() {
        this.items.clear()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(this.items[position])
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun setAll(data: Collection<E>): Boolean {
        this.items.clear()
        val ret = this.items.addAll(data)
        this.notifyDataSetChanged()
        return ret
    }

}
