package com.arman.queuetube.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.*
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.util.notifications.receivers.WifiReceiver
import com.arman.queuetube.util.services.KillNotificationService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), PlaylistFragment.OnPlayItemsListener {

    private var extendedToolbar: LinearLayout? = null

    private var navigationView: BottomNavigationView? = null

    private var mainFragment: MainFragment? = null
    private var trendingFragment: TrendingFragment? = null
    private var discoverFragment: DiscoverFragment? = null
    private var libraryFragment: LibraryFragment? = null

    private var wifiReceiver: WifiReceiver? = null

    private var currentFragment: Int = 0

    private fun setupWifiReceiver() {
        this.wifiReceiver = WifiReceiver()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(this.wifiReceiver, filter)
    }

    private fun setupActionBar() {
        val toolbar = this.findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun setupNavigationView() {
        this.navigationView = this.findViewById<View>(R.id.bottom_nav_bar) as BottomNavigationView
    }

    private fun setupPlaylistFragment(playlistName: String): PlaylistFragment {
        val bundle = Bundle()
        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, playlistName)
        val playlistFragment = PlaylistFragment()
        playlistFragment.arguments = bundle
        playlistFragment.setOnPlayItemsListener(this)
        return playlistFragment
    }

    fun switchToMainFragment() {
        if (this.currentFragment != Constants.Fragment.MAIN) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.mainFragment != null) {
                transaction.show(this.mainFragment!!)
                this.refreshVideoFavorited()
            } else {
                this.mainFragment = MainFragment()
                transaction.add(R.id.content_frame, this.mainFragment!!)
            }
            this.trendingFragment?.let { transaction.hide(this.trendingFragment!!) }
            this.discoverFragment?.let { transaction.hide(this.discoverFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            this.extendedToolbar?.visibility = View.VISIBLE
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.MAIN
        }
    }

    fun switchToTrendingFragment() {
        if (this.currentFragment != Constants.Fragment.TRENDING) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.trendingFragment != null) {
                transaction.show(this.trendingFragment!!)
                this.refreshVideoFavorited()
            } else {
                this.trendingFragment = TrendingFragment()
                transaction.add(R.id.content_frame, this.trendingFragment!!)
            }
            this.mainFragment?.let { transaction.hide(this.mainFragment!!) }
            this.discoverFragment?.let { transaction.hide(this.discoverFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            this.extendedToolbar?.visibility = View.VISIBLE
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.TRENDING
        }
    }

    fun switchToDiscoverFragment() {
        if (this.currentFragment != Constants.Fragment.DISCOVER) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.discoverFragment != null) {
                transaction.show(this.discoverFragment!!)
                this.refreshVideoFavorited()
            } else {
                this.discoverFragment = DiscoverFragment()
                transaction.add(R.id.content_frame, this.discoverFragment!!)
            }
            this.mainFragment?.let { transaction.hide(this.mainFragment!!) }
            this.trendingFragment?.let { transaction.hide(this.trendingFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            this.extendedToolbar?.visibility = View.VISIBLE
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.DISCOVER
        }
    }

    fun switchToLibraryFragment() {
        if (this.currentFragment != Constants.Fragment.LIBRARY) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.libraryFragment != null) {
                transaction.show(this.libraryFragment!!)
                this.refreshVideoFavorited()
            } else {
                this.libraryFragment = LibraryFragment()
                transaction.add(R.id.content_frame, this.libraryFragment!!)
            }
            this.mainFragment?.let { transaction.hide(this.mainFragment!!) }
            this.trendingFragment?.let { transaction.hide(this.trendingFragment!!) }
            this.discoverFragment?.let { transaction.hide(this.discoverFragment!!) }
            this.extendedToolbar?.visibility = View.VISIBLE
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.LIBRARY
        }
    }

    private fun refreshVideoFavorited() {
        val playerFragment = this.mainFragment!!.pagerAdapter?.playerFragment as PlayerFragment
        val currentVideo = playerFragment.currentVideo
        playerFragment.updateVideo(GsonPlaylistHelper.isFavorited(currentVideo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GsonPlaylistHelper.onCreate(this)

        this.currentFragment = -1

        startService(Intent(this, KillNotificationService::class.java))

        this.extendedToolbar = findViewById<View>(R.id.main_extended_toolbar) as LinearLayout

        setupWifiReceiver()
        setupActionBar()
        setupNavigationView()
        switchToMainFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (this.currentFragment == Constants.Fragment.MAIN) {
            val viewPager = this.mainFragment!!.viewPager
            if (viewPager?.currentItem == 0) {
                super.onBackPressed()
            } else {
                viewPager?.currentItem = viewPager?.currentItem!! - 1
            }
        } else {
            this.navigationView!!.menu.findItem(R.id.nav_item_home).isChecked = true
            this.switchToMainFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(this.wifiReceiver)
    }

    fun refreshPlaylists() {

    }

    override fun onPlayAll(videos: Collection<VideoData>) {
        val playerFragment = this.mainFragment!!.pagerAdapter?.playerFragment as PlayerFragment
        if (playerFragment.setQueueTo(videos)) {
            playerFragment.forcePlayNext()
        }
    }

    override fun onPlay(video: VideoData) {
        val playerFragment = this.mainFragment!!.pagerAdapter?.playerFragment as PlayerFragment
        if (playerFragment.addToQueue(video)) {
            playerFragment.tryPlayNext()
        }
    }

    companion object {

        val TAG = "MainActivity"

    }

}
