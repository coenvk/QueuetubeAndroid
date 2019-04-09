package com.arman.queuetube.fragments.pager

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.arman.queuetube.fragments.main.QueueFragment
import com.arman.queuetube.fragments.main.SearchFragment
import java.util.*

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val fragments: MutableList<Fragment>
    private val fragmentTitles: MutableList<String>

    init {
        this.fragments = LinkedList()
        this.fragmentTitles = LinkedList()
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

    fun addFragment(fragment: Fragment) {
        if (fragment is QueueFragment) {
            this.fragmentTitles.add(PAGE_TITLES[QUEUE_INDEX])
        } else if (fragment is SearchFragment) {
            this.fragmentTitles.add(PAGE_TITLES[RECOMMENDED_INDEX])
        }
        this.fragments.add(fragment)
    }

    companion object {

        const val TAG = "ViewPagerAdapter"

        const val NUM_PAGES = 2

        const val QUEUE_INDEX = 0
        const val RECOMMENDED_INDEX = 1

        @JvmField
        val PAGE_TITLES = arrayOf("Queue", "Recommended")

    }

}
