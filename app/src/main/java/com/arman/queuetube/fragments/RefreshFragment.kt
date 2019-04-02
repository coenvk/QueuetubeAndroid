package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.listeners.OnRefreshListener

abstract class RefreshFragment : Fragment() {

    private var refreshLayout: SwipeRefreshLayout? = null
    var onRefreshListener: OnRefreshListener? = null

    protected open var isRefreshable: Boolean = true
    protected open var loadOnStart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.isRefreshable = arguments.getBoolean(Constants.Fragment.Argument.IS_REFRESHABLE, true)
            this.loadOnStart = arguments.getBoolean(Constants.Fragment.Argument.LOAD_ON_START, true)
        }
        this.onRefreshListener = OnRefreshListener { load() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this.isRefreshable) {
            this.refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
            this.refreshLayout?.setOnRefreshListener { onRefreshListener?.onRefresh() }
        }
    }

    open fun finishRefresh() {
        this.refreshLayout?.isRefreshing = false
    }

    abstract fun load()

    override fun onStart() {
        super.onStart()
        if (this.loadOnStart) {
            this.load()
        }
    }

}