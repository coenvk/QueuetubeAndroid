package com.arman.queuetube.listeners

inline fun OnRefreshListener(
        crossinline onRefresh: () -> Unit = {}
): OnRefreshListener {
    return object : OnRefreshListener {
        override fun onRefresh() {
            onRefresh()
        }
    }
}

interface OnRefreshListener {

    fun onRefresh()

}