package com.arman.queuetube.listeners

inline fun <T> OnTaskFinishedListener(
        crossinline onTaskFinished: (T) -> Unit = {}
): OnTaskFinishedListener<T> {
    return object : OnTaskFinishedListener<T> {
        override fun onTaskFinished(result: T) {
            onTaskFinished(result)
        }
    }
}

interface OnTaskFinishedListener<T> {

    fun onTaskFinished(result: T)

}