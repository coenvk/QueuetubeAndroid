package com.arman.queuetube.fragments.pager

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arman.queuetube.R
import com.arman.queuetube.fragments.main.PlayerFragment
import com.arman.queuetube.fragments.main.QueueFragment
import com.arman.queuetube.fragments.main.SearchFragment
import com.arman.queuetube.model.adapters.BaseTouchAdapter
import java.util.*

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var playerFragment: Fragment? = null
        private set

    private val fragments: MutableList<Fragment>
    private val fragmentTitles: MutableList<String>

    init {
        this.fragments = LinkedList()
        this.fragmentTitles = LinkedList()

        this.addFragment(QueueFragment())
        this.addFragment(SearchFragment())

        this.setupPlayer(fm)
    }

    private fun setupPlayer(fm: FragmentManager) {
        this.playerFragment = PlayerFragment()
        fm.beginTransaction().replace(R.id.top_player, this.playerFragment!!).commit()
    }

    override fun getItem(position: Int): Fragment {
        return this.fragments[position % count]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return this.fragmentTitles[position % count]
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    fun getFragmentById(@IdRes id: Int): Fragment? {
        for (fragment in this.fragments) {
            if (fragment.id == id) {
                return fragment
            }
        }
        return null
    }

    fun getFragmentByIndex(index: Int): Fragment {
        return this.fragments[index]
    }

    fun getFragmentByPageTitle(title: String): Fragment? {
        val index = this.fragmentTitles.indexOf(title)
        return if (index >= 0 && index < this.fragments.size) {
            this.fragments[index]
        } else null
    }

    private fun addFragment(fragment: Fragment) {
        if (fragment is QueueFragment) {
            this.fragmentTitles.add(PAGE_TITLES[QUEUE_INDEX])
        } else if (fragment is SearchFragment) {
            this.fragmentTitles.add(PAGE_TITLES[SEARCH_INDEX])
        }
        this.fragments.add(fragment)
    }

    inner class QueueItemClickListener : BaseTouchAdapter.OnItemClickListener {

        override fun onItemClick(holder: RecyclerView.ViewHolder) {
            holder.itemView.isClickable = false
            val queueFragment = this@ViewPagerAdapter.getFragmentByIndex(QUEUE_INDEX) as QueueFragment

            val animation = AnimationUtils.loadAnimation(queueFragment.activity, R.anim.remove_from_queue)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    queueFragment.listAdapter?.remove(holder.adapterPosition)
                    if (queueFragment.listAdapter?.isEmpty!!) {
                        queueFragment.showEmptyText()
                    }
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })

            holder.itemView.startAnimation(animation)
        }

    }

    inner class SearchItemClickListener : BaseTouchAdapter.OnItemClickListener {

        override fun onItemClick(holder: RecyclerView.ViewHolder) {
            val queueFragment = this@ViewPagerAdapter.getFragmentByIndex(QUEUE_INDEX) as QueueFragment
            val searchFragment = this@ViewPagerAdapter.getFragmentByIndex(SEARCH_INDEX) as SearchFragment

            val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.add_from_search_results)
            holder.itemView.startAnimation(animation)

            queueFragment.listAdapter?.add(searchFragment.listAdapter?.get(holder.adapterPosition)!!)
            (this@ViewPagerAdapter.playerFragment as PlayerFragment).tryPlayNext()
        }

    }

    companion object {

        val TAG = "ViewPagerAdapter"

        const val NUM_PAGES = 2

        const val QUEUE_INDEX = 0
        const val SEARCH_INDEX = 1

        @JvmField
        val PAGE_TITLES = arrayOf("Queue", "Search")

    }

}
