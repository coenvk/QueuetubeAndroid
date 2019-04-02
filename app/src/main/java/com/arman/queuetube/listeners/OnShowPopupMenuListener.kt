package com.arman.queuetube.listeners

import android.view.View
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.model.viewholders.BaseViewHolder

inline fun OnShowPopupMenuListener(
        crossinline onShowPopupMenu: (BaseViewHolder<VideoData>, View) -> Unit
): OnShowPopupMenuListener {
    return object : OnShowPopupMenuListener {
        override fun onShowPopupMenu(holder: BaseViewHolder<VideoData>, anchorView: View) {
            onShowPopupMenu(holder, anchorView)
        }
    }
}

interface OnShowPopupMenuListener {

    fun onShowPopupMenu(holder: BaseViewHolder<VideoData>, anchorView: View)

}