package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.View
import com.arman.queuetube.listeners.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.*

abstract class RefreshVideoListFragment : AsyncVideoListFragment(), OnRefreshListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh_layout?.setOnRefreshListener { onRefresh() }
    }

    override fun onRefresh() {
        load()
    }

    override fun finishLoad() {
        super.finishLoad()
        swipe_refresh_layout?.isRefreshing = false
    }

}