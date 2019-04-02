package com.arman.queuetube.fragments

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.main.PlayerFragment
import com.arman.queuetube.fragments.main.SearchFragment
import com.arman.queuetube.fragments.pager.ViewPagerAdapter
import com.arman.queuetube.util.notifications.receivers.NotificationReceiver
import com.arman.queuetube.util.transformers.DepthPageTransformer
import com.google.android.material.tabs.TabLayout

class MainFragment : Fragment() {

    var viewPager: ViewPager? = null
        private set
    var pagerAdapter: ViewPagerAdapter? = null
        private set

    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false) as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupPager(view)
        this.setupReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        val view = item.actionView as SearchView
        this.addSearchListeners(view)
    }

    private fun setupPager(view: View) {
        this.viewPager = view.findViewById(R.id.view_pager) as ViewPager

        val tabLayout = activity!!.findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(this.viewPager)

        this.pagerAdapter = ViewPagerAdapter(fragmentManager!!)
        this.viewPager!!.adapter = this.pagerAdapter
        this.viewPager!!.setPageTransformer(true, DepthPageTransformer())
    }

    private fun addSearchListeners(view: SearchView) {
        view.setOnSearchClickListener { this@MainFragment.viewPager!!.currentItem = ViewPagerAdapter.SEARCH_INDEX }
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                this@MainFragment.viewPager!!.currentItem = ViewPagerAdapter.SEARCH_INDEX
                val searchFragment = this@MainFragment.pagerAdapter!!.getFragmentByIndex(ViewPagerAdapter.SEARCH_INDEX) as SearchFragment
                searchFragment.load(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

    private fun setupReceiver() {
        this.broadcastReceiver = NotificationReceiver(this.pagerAdapter!!.playerFragment as PlayerFragment)
        val filter = IntentFilter()
        filter.addAction(Constants.Action.NEXT_ACTION)
        filter.addAction(Constants.Action.MAIN_ACTION)
        filter.addAction(Constants.Action.PAUSE_ACTION)
        filter.addAction(Constants.Action.PLAY_ACTION)
        filter.addAction(Constants.Action.STOP_ACTION)
        activity!!.registerReceiver(this.broadcastReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(this.broadcastReceiver)
    }

}
