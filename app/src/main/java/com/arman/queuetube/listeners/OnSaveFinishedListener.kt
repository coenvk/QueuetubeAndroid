package com.arman.queuetube.listeners

inline fun OnSaveFinishedListener(
        crossinline onSaveFinished: () -> Unit = {}
): OnSaveFinishedListener {
    return object : OnSaveFinishedListener {
        override fun onSaveFinished() {
            onSaveFinished()
        }
    }
}

interface OnSaveFinishedListener {

    fun onSaveFinished()

}