package com.arman.queuetube.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import com.arman.queuetube.config.Constants
import com.arman.queuetube.listeners.OnTaskFinishedListener
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.BaseTask
import kotlinx.android.synthetic.main.fragment_list.*

abstract class AsyncVideoListFragment : VideoListFragment(), OnTaskFinishedListener<MutableList<VideoData>> {

    protected open var loadOnStart: Boolean = true

    private var task: BaseTask<String, MutableList<VideoData>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.loadOnStart = arguments.getBoolean(Constants.Fragment.Argument.LOAD_ON_START, true)
        }
    }

    abstract fun doInBackground(params: Array<out String>): MutableList<VideoData>

    private fun doLoad(params: Array<out String>) {
        list_load_layout?.visibility = View.VISIBLE
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

    override fun onStart() {
        super.onStart()
        if (this.loadOnStart) {
            this.load()
        }
    }

    open fun finishLoad() {
        list_load_layout?.visibility = View.GONE
        val videoCount = this.listAdapter!!.itemCount
        if (videoCount <= 0) {
            list_play_all_button?.visibility = View.GONE
            showEmptyText()
        } else {
            showList()
            scrollToTop()
            list_play_all_button?.visibility = View.VISIBLE
        }
    }

    protected fun load() {
        this.load("")
    }

    open fun load(vararg params: String) {
        this.doLoad(params)
    }

    override fun onTaskFinished(result: MutableList<VideoData>) {
        this.listAdapter!!.setAll(result)
        this.finishLoad()
    }

}