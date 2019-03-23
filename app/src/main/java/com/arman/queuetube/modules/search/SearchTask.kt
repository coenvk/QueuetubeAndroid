package com.arman.queuetube.modules.search

import android.os.AsyncTask

import com.arman.queuetube.fragments.SearchFragment
import com.arman.queuetube.model.VideoData

class SearchTask(private val ytSearcher: YouTubeSearcher, private val searchFragment: SearchFragment) : AsyncTask<String, Int, MutableList<VideoData>>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(videoData: MutableList<VideoData>) {
        super.onPostExecute(videoData)
        this.searchFragment.resultsAdapter!!.setAll(videoData)
        if (!videoData.isEmpty()) {
            this.searchFragment.showResults()
            this.searchFragment.scrollToTop()
        } else {
            this.searchFragment.showEmptyText()
        }
    }

    protected override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        super.onProgressUpdate(*values)
    }

    override fun onCancelled(videoData: MutableList<VideoData>) {
        super.onCancelled(videoData)
    }

    override fun onCancelled() {
        super.onCancelled()
    }

    override fun doInBackground(vararg strings: String): MutableList<VideoData> {
        return ytSearcher.search(strings[0])
    }

}
