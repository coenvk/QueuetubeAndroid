package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.arman.queuetube.R
import com.arman.queuetube.model.adapters.VideoItemAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.model.adapters.BaseTouchAdapter

class SearchFragment : Fragment() {

    private var emptyTextLayout: LinearLayout? = null

    private var resultsView: RecyclerView? = null
    var resultsAdapter: VideoItemAdapter? = null
        private set
    private var layoutManager: RecyclerView.LayoutManager? = null

    private var onItemClickListener: BaseTouchAdapter.OnItemClickListener? = null

    fun scrollToTop() {
        if (this.resultsView != null && this.layoutManager != null) {
            this.layoutManager!!.smoothScrollToPosition(this.resultsView, null, 0)
        }
    }

    fun setOnItemClickListener(onItemClickListener: BaseTouchAdapter.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun showEmptyText() {
        this.resultsView!!.visibility = View.GONE
        this.emptyTextLayout!!.visibility = View.VISIBLE
    }

    fun showResults() {
        this.emptyTextLayout!!.visibility = View.GONE
        this.resultsView!!.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.emptyTextLayout = view.findViewById<View>(R.id.search_results_empty_text_layout) as LinearLayout

        this.resultsView = view.findViewById<View>(R.id.search_results) as RecyclerView
        this.resultsView!!.setHasFixedSize(true)
        this.layoutManager = LinearLayoutManager(activity)
        this.resultsView!!.layoutManager = this.layoutManager

        this.resultsAdapter = VideoItemAdapter(this.onItemClickListener)

        this.resultsView!!.adapter = this.resultsAdapter
    }

}
