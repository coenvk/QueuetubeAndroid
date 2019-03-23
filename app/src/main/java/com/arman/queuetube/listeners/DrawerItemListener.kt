package com.arman.queuetube.listeners

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.arman.queuetube.R
import com.arman.queuetube.activities.MainActivity
import com.arman.queuetube.activities.SettingsActivity
import com.google.android.material.navigation.NavigationView

class DrawerItemListener(private val activity: MainActivity, private val drawerLayout: DrawerLayout) : NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private var switchFragment: Boolean = false
    private var fragmentResId: Int = 0

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (item.isCheckable) {
            item.isChecked = true
            this.fragmentResId = id
            this.switchFragment = true
        } else {
            when (id) {
                R.id.menu_item_settings -> {
                    this.activity.startActivity(Intent(activity, SettingsActivity::class.java))
                    return true
                }
                R.id.menu_item_rate -> {
                    this.launchMarket()
                    return true
                }
                R.id.menu_item_buy_pro -> return true
                R.id.menu_item_info -> return true
                else -> return false
            }
        }

        this.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun launchMarket() {
        val appPackageName = activity.packageName
        var uri = Uri.parse("market://details?id=$appPackageName")
        val action = Intent.ACTION_VIEW
        val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var marketIntent: Intent
        try {
            marketIntent = Intent(action, uri)
            marketIntent.setPackage("com.android.vending")
            marketIntent.flags = flags
            activity.startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            uri = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            marketIntent = Intent(action, uri)
            marketIntent.flags = flags
            activity.startActivity(marketIntent)
        }

    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

    }

    override fun onDrawerOpened(drawerView: View) {

    }

    override fun onDrawerClosed(drawerView: View) {
        if (this.switchFragment) {
            when (this.fragmentResId) {
                R.id.menu_item_home -> activity.switchToMainFragment()
            }
            this.switchFragment = false
        }
    }

    override fun onDrawerStateChanged(newState: Int) {

    }

}
