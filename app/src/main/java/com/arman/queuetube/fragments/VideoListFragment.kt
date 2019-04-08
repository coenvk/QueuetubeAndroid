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
import kotlinx.android.synthetic.main.fragment_list.*

abstract class VideoListFragment : VideoItemFragment() {

    protected open var isDraggable: Boolean = false
    protected var isSortable: Boolean = true
    protected var isShufflable: Boolean = true

    var listAdapter: VideoItemAdapter? = null
        protected set

    private var layoutManager: RecyclerView.LayoutManager? = null

    protected var itemTouchHelper: ItemTouchHelper? = null

    open fun onShuffle() {
        this.onPlayItemsListener?.onShuffle(this.listAdapter!!.all)
    }

    open fun onSort() {
        // TODO
    }

    open fun onPlayAll() {
        this.onPlayItemsListener?.onPlayAll(this.listAdapter!!.all)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
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

        list_view.setHasFixedSize(true)

        this.listAdapter = VideoItemAdapter(this, this)
        list_view.adapter = this.listAdapter

        list_view.setItemViewCacheSize(25)

        list_play_all_button?.setOnClickListener { onPlayAll() }

        if (this.isDraggable) {
            val callback = VideoItemTouchHelper.Callback(this.listAdapter!!)
            this.itemTouchHelper = ItemTouchHelper(callback)
            this.itemTouchHelper!!.attachToRecyclerView(list_view)
        }
    }

    fun scrollToTop() {
        if (list_view != null && this.layoutManager != null) {
            this.layoutManager!!.smoothScrollToPosition(list_view, null, 0)
        }
    }

    fun showEmptyText() {
        list_empty_text_layout?.visibility = View.VISIBLE
    }

    fun showList() {
        list_empty_text_layout?.visibility = View.GONE
    }

    companion object {

        const val TAG = "VideoListFragment"

    }

}