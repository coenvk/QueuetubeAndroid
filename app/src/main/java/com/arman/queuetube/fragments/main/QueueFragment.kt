package com.arman.queuetube.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import com.arman.queuetube.model.adapters.VideoItemAdapter
import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper

class QueueFragment : Fragment() {

    private var emptyTextLayout: LinearLayout? = null

    private var queueView: RecyclerView? = null
    var queueAdapter: VideoItemAdapter? = null
        private set
    private var layoutManager: RecyclerView.LayoutManager? = null

    var onItemClickListener: BaseTouchAdapter.OnItemClickListener? = null
    var onItemDragListener: BaseTouchAdapter.OnItemDragListener? = null

    private var itemTouchHelper: ItemTouchHelper? = null

    fun showEmptyText() {
        this.queueView!!.visibility = View.GONE
        this.emptyTextLayout!!.visibility = View.VISIBLE
    }

    fun showQueue() {
        this.emptyTextLayout!!.visibility = View.GONE
        this.queueView!!.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_queue, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.emptyTextLayout = view.findViewById(R.id.queue_empty_text_layout) as LinearLayout

        this.queueView = view.findViewById(R.id.list_view) as RecyclerView
        this.queueView!!.setHasFixedSize(true)
        this.layoutManager = LinearLayoutManager(activity)
        this.queueView!!.layoutManager = this.layoutManager

        if (this.onItemDragListener == null) {
            this.onItemDragListener = object : BaseTouchAdapter.OnItemDragListener {
                override fun onItemDrag(holder: RecyclerView.ViewHolder) {
                    this@QueueFragment.itemTouchHelper!!.startDrag(holder)
                }
            }
        }

        this.queueAdapter = VideoItemAdapter(this.onItemClickListener, this.onItemDragListener)
        this.queueView!!.adapter = this.queueAdapter

        val callback = VideoItemTouchHelper.Callback(this.queueAdapter!!)
        this.itemTouchHelper = ItemTouchHelper(callback)
        this.itemTouchHelper!!.attachToRecyclerView(this.queueView)

    }

}
