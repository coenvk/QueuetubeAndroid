package com.arman.queuetube.fragments.main

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.SearchView
import com.arman.queuetube.R
import com.arman.queuetube.fragments.VideoListFragment
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.search.YouTubeSearcher

class SearchFragment : VideoListFragment() {

    private var emptyTextLayout: LinearLayout? = null
    override var loadOnStart: Boolean = false
        get() = false
    override var isRefreshable: Boolean = false
        get() = false

    fun showEmptyText() {
        this.emptyTextLayout!!.visibility = View.VISIBLE
    }

    fun showResults() {
        this.emptyTextLayout!!.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        val view = item.actionView as SearchView
        this.addSearchListeners(view)
    }

    private fun addSearchListeners(view: SearchView) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                load(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.emptyTextLayout = view.findViewById(R.id.search_results_empty_text_layout) as LinearLayout
    }

    override fun finishRefresh() {
        if (!listAdapter?.isEmpty!!) {
            showResults()
            scrollToTop()
        } else {
            showEmptyText()
        }
    }

    override fun onSaveFinished() = Unit

    override fun doInBackground(params: Array<out String>): MutableList<VideoData> {
        return YouTubeSearcher.search(params[0])
    }

}
