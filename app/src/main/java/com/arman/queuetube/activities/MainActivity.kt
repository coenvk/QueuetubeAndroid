package com.arman.queuetube.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.main.HomeFragment
import com.arman.queuetube.fragments.main.LibraryFragment
import com.arman.queuetube.fragments.main.PlayerFragment
import com.arman.queuetube.fragments.main.SearchFragment
import com.arman.queuetube.listeners.OnPlayItemsListener
import com.arman.queuetube.listeners.events.PlayEvent
import com.arman.queuetube.model.VideoData
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.receivers.WifiReceiver
import com.arman.queuetube.services.KillNotificationService
import com.arman.queuetube.util.getEnumExtra
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnPlayItemsListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private var playerFragment: PlayerFragment? = null

    private var homeFragment: HomeFragment? = null
    private var searchFragment: SearchFragment? = null
    private var libraryFragment: LibraryFragment? = null

    private var wifiReceiver: WifiReceiver? = null

    private var currentFragment: Int = 0

    private fun enableScroll() {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        toolbar.layoutParams = params
    }

    private fun disableScroll() {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
        toolbar.layoutParams = params
        appbar.setExpanded(true, false)
    }

    private fun setupWifiReceiver() {
        this.wifiReceiver = WifiReceiver()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(this.wifiReceiver, filter)
    }

    private fun setupPlayerFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        if (this.playerFragment != null) {
            transaction.show(this.playerFragment!!)
            this.refreshVideoFavorited()
        } else {
            this.playerFragment = PlayerFragment()
            transaction.add(R.id.player_container, this.playerFragment!!)
        }
        transaction.commitNow()
    }

    fun switchToHomeFragment() {
        if (this.currentFragment != Constants.Fragment.HOME) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.homeFragment != null) {
                transaction.show(this.homeFragment!!)
            } else {
                this.homeFragment = HomeFragment()
                this.homeFragment!!.onPlayItemsListener = this
                transaction.add(R.id.content_frame, this.homeFragment!!)
            }
            this.searchFragment?.let { transaction.hide(this.searchFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.HOME
            this.enableScroll()
        }
    }

    fun switchToSearchFragment() {
        if (this.currentFragment != Constants.Fragment.SEARCH) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.searchFragment != null) {
                transaction.show(this.searchFragment!!)
            } else {
                this.searchFragment = SearchFragment()
                this.searchFragment!!.onPlayItemsListener = this
                transaction.add(R.id.content_frame, this.searchFragment!!)
            }
            this.homeFragment?.let { transaction.hide(this.homeFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.SEARCH
            this.disableScroll()
        }
    }

    fun switchToLibraryFragment() {
        if (this.currentFragment != Constants.Fragment.LIBRARY) {
            val transaction = supportFragmentManager.beginTransaction()
            if (this.libraryFragment != null) {
                transaction.show(this.libraryFragment!!)
            } else {
                this.libraryFragment = LibraryFragment()
                this.libraryFragment!!.onPlayItemsListener = this
                transaction.add(R.id.content_frame, this.libraryFragment!!)
            }
            this.homeFragment?.let { transaction.hide(this.homeFragment!!) }
            this.searchFragment?.let { transaction.hide(this.searchFragment!!) }
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.LIBRARY
            this.enableScroll()
        }
    }

    private fun refreshVideoFavorited() {
        this.playerFragment?.let {
            val currentVideo = playerFragment!!.currentVideo
            currentVideo?.let { playerFragment!!.updateVideo(GsonPlaylistHelper.isFavorited(currentVideo)) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GsonPlaylistHelper.onCreate(this)

        this.currentFragment = -1

        startService(Intent(this, KillNotificationService::class.java))

        setupWifiReceiver()
        setSupportActionBar(toolbar)
        bottom_nav_bar.setOnNavigationItemSelectedListener(this)
        switchToHomeFragment()
        setupPlayerFragment()

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val enumValue = intent.getEnumExtra<PlayEvent>()
            val videos = intent.getParcelableArrayListExtra<VideoData>(Constants.Fragment.Argument.VIDEO_LIST)
            if (enumValue != null) {
                when (enumValue) {
                    PlayEvent.PLAY -> onPlay(videos.first())
                    PlayEvent.PLAY_NEXT -> onPlayNext(videos.first())
                    PlayEvent.PLAY_NOW -> onPlayNow(videos.first())
                    PlayEvent.PLAY_ALL -> onPlayAll(videos)
                    PlayEvent.SHUFFLE -> onShuffle(videos)
                }
            } else {
                val action = intent.action
                if (action == Intent.ACTION_SEND) {
                    val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                    println(text)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openSettings(item: MenuItem) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

//    override fun onBackPressed() {
//        if (this.currentFragment == Constants.Fragment.HOME) {
//            val viewPager = this.mainFragment!!.viewPager
//            if (viewPager?.currentItem == 0) {
//                super.onBackPressed()
//            } else {
//                viewPager?.currentItem = viewPager?.currentItem!! - 1
//            }
//        } else {
//            this.navigationView!!.menu.findItem(R.id.nav_item_home).isChecked = true
//            this.switchToHomeFragment()
//        }
//    }

    override fun onBackPressed() {
        bottom_nav_bar.menu.findItem(R.id.nav_item_home).isChecked = true
        this.switchToHomeFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(this.wifiReceiver)
    }

    override fun onPlayAll(videos: Collection<VideoData>) {
        if (playerFragment != null && videos.isNotEmpty() && playerFragment!!.setQueueTo(videos)) {
            playerFragment!!.forcePlayNext()
        }
    }

    override fun onPlay(video: VideoData) {
        if (playerFragment != null && playerFragment!!.addToQueue(video)) {
            playerFragment!!.tryPlayNext()
        }
    }

    override fun onShuffle(videos: Collection<VideoData>) {
        onPlayAll(videos.shuffled())
    }

    override fun onPlayNext(video: VideoData) {
        if (playerFragment != null && playerFragment!!.addToQueue(0, video)) {
            playerFragment!!.tryPlayNext()
        }
    }

    override fun onPlayNow(video: VideoData) {
        if (playerFragment != null && playerFragment!!.addToQueue(0, video)) {
            playerFragment!!.forcePlayNext()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        this.playerFragment?.swipeDown()
        return when (item.itemId) {
            R.id.nav_item_home -> {
                switchToHomeFragment()
                true
            }
            R.id.nav_item_search -> {
                switchToSearchFragment()
                true
            }
            R.id.nav_item_library -> {
                switchToLibraryFragment()
                true
            }
            else -> false
        }
    }

    companion object {

        const val TAG = "MainActivity"

    }

}
