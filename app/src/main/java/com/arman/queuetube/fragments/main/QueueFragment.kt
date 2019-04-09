package com.arman.queuetube.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.fragments.VideoListFragment
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.viewholders.BaseViewHolder

class QueueFragment : VideoListFragment() {

    @MenuRes
    override val popupMenuResId: Int = R.menu.popup_menu_queue_item

    override fun onRemoveFromPlaylist(holder: BaseViewHolder<VideoData>) {
        this.onItemClick(holder)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_queue, container, false)
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder) {
        holder.itemView.isClickable = false

        val animation = AnimationUtils.loadAnimation(this@QueueFragment.activity, R.anim.remove_from_queue)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                this@QueueFragment.listAdapter?.remove(holder.adapterPosition)
                if (this@QueueFragment.listAdapter?.isEmpty!!) {
                    this@QueueFragment.showEmptyText()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        holder.itemView.startAnimation(animation)
    }

}
