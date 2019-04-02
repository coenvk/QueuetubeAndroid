package com.arman.queuetube.fragments

import android.os.AsyncTask
import com.arman.queuetube.listeners.OnTaskFinishedListener
import com.arman.queuetube.modules.BaseTask

abstract class AsyncFragment<Params, Result> : RefreshFragment(), OnTaskFinishedListener<Result> {

    private var task: BaseTask<Params, Result>? = null
    var params: Array<out Params>? = null

    abstract fun doInBackground(params: Array<out Params>): Result

    private fun doLoad(params: Array<out Params>) {
        if (task == null) {
            task = BaseTask(::doInBackground)
            task!!.onTaskFinishedListener = this
        }
        val status = task!!.status
        if (status != AsyncTask.Status.RUNNING) {
            if (status == AsyncTask.Status.FINISHED) {
                task = BaseTask(::doInBackground)
                task!!.onTaskFinishedListener = this
            }
            task!!.execute(*params)
        }
    }

    override fun load() {
        if (this.params != null) {
            this.doLoad(this.params!!)
        } else {
            this.finishRefresh()
        }
    }

    fun load(vararg params: Params) {
        this.doLoad(params)
    }

    override fun onTaskFinished(result: Result) {
        this.finishRefresh()
    }

}