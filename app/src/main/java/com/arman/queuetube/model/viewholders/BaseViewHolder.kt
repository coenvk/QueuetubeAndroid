package com.arman.queuetube.model.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<E>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var item: E? = null
        protected set

    fun bind(item: E) {
        this.item = item
    }

    open fun unbind() {

    }

}
