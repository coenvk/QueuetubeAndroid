package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arman.queuetube.R
import com.arman.queuetube.listeners.OnRefreshListener

abstract class RefreshVideoListFragment : AsyncVideoListFragment(), OnRefreshListener {

    private var refreshLayout: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        this.refreshLayout?.setOnRefreshListener { onRefresh() }
    }

    override fun onRefresh() {
        load()
    }

    override fun finishLoad() {
        super.finishLoad()
        this.refreshLayout?.isRefreshing = false
    }

}