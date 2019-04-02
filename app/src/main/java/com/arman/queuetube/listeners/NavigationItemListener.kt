package com.arman.queuetube.listeners

import android.view.MenuItem
import com.arman.queuetube.R
import com.arman.queuetube.activities.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationItemListener(private val activity: MainActivity) : BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_item_home -> {
                activity.switchToHomeFragment()
                true
            }
            R.id.nav_item_search -> {
                activity.switchToSearchFragment()
                true
            }
            R.id.nav_item_library -> {
                activity.switchToLibraryFragment()
                true
            }
            else -> false
        }
    }

}