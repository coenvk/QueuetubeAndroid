package com.arman.queuetube.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.model.adapters.VideoItemAdapter
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class VideoListFragment : VideoItemFragment() {

    protected open var isDraggable: Boolean = false
    protected var isSortable: Boolean = true
    protected var isShufflable: Boolean = true

    protected var emptyTextLayout: View? = null

    protected var listView: RecyclerView? = null
    var listAdapter: VideoItemAdapter? = null
        protected set

    private var layoutManager: RecyclerView.LayoutManager? = null

    protected var playAllButton: FloatingActionButton? = null

    protected var itemTouchHelper: ItemTouchHelper? = null

    open fun onShuffle() {
        this.onPlayItemsListener?.onShuffle(this.listAdapter!!.all)
    }

    open fun onSort() {
        // TODO
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false) as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.isDraggable = arguments.getBoolean(Constants.Fragment.Argument.IS_DRAGGABLE, this.isDraggable)
            this.isSortable = arguments.getBoolean(Constants.Fragment.Argument.IS_SORTABLE, this.isSortable)
            this.isShufflable = arguments.getBoolean(Constants.Fragment.Argument.IS_SHUFFLABLE, this.isShufflable)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.emptyTextLayout = view.findViewById(R.id.list_empty_text_layout)

        this.listView = view.findViewById(R.id.list_view)
        this.listView!!.setHasFixedSize(true)

        this.listAdapter = VideoItemAdapter(this, this)
        this.listView!!.adapter = this.listAdapter

        this.listView!!.setItemViewCacheSize(25)

        this.playAllButton = view.findViewById(R.id.list_play_all_button)
        this.playAllButton?.setOnClickListener {
            this@VideoListFragment.onPlayItemsListener?.onPlayAll(this@VideoListFragment.listAdapter!!.all)
        }

        if (this.isDraggable) {
            val callback = VideoItemTouchHelper.Callback(this.listAdapter!!)
            this.itemTouchHelper = ItemTouchHelper(callback)
            this.itemTouchHelper!!.attachToRecyclerView(this.listView)
        }
    }

    fun scrollToTop() {
        if (this.listView != null && this.layoutManager != null) {
            this.layoutManager!!.smoothScrollToPosition(this.listView, null, 0)
        }
    }

    fun showEmptyText() {
        this.emptyTextLayout?.visibility = View.VISIBLE
    }

    fun showList() {
        this.emptyTextLayout?.visibility = View.GONE
    }

    companion object {

        const val TAG = "VideoListFragment"

    }

}