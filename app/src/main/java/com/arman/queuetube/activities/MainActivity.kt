package com.arman.queuetube.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arman.queuetube.R
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.main.HomeFragment
import com.arman.queuetube.fragments.main.LibraryFragment
import com.arman.queuetube.fragments.main.PlayerFragment
import com.arman.queuetube.fragments.main.SearchFragment
import com.arman.queuetube.listeners.OnPlayItemsListener
import com.arman.queuetube.model.Video
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import com.arman.queuetube.modules.search.YouTubeService
import com.arman.queuetube.receivers.PlayReceiver
import com.arman.queuetube.receivers.WifiReceiver
import com.arman.queuetube.services.KillNotificationService
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity(), OnPlayItemsListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private var playerFragment: PlayerFragment? = null

    private var homeFragment: HomeFragment? = null
    private var searchFragment: SearchFragment? = null
    private var libraryFragment: LibraryFragment? = null

    private var wifiReceiver: WifiReceiver? = null
    private var playReceiver: PlayReceiver? = null

    private var currentFragment: Int = 0

    private var savedInstanceState: Bundle? = null

    private var bottomNavUp: Boolean = true

    fun enableScroll(): Int {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        val flags = params.scrollFlags
        params.scrollFlags =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        toolbar.layoutParams = params
        return flags
    }

    fun setScroll(scrollFlags: Int) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = scrollFlags
        toolbar.layoutParams = params
    }

    fun disableScroll() {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
        toolbar.layoutParams = params
    }

    fun hideBottomBar() {
        if (bottomNavUp) {
            with(bottom_nav_bar, {
                animate()
                        .translationY(height.toFloat())
                        .alpha(0f)
                        .withEndAction {
                            visibility = View.GONE
                            bottomNavUp = false
                        }
            })
        }
    }

    fun showBottomBar() {
        if (!bottomNavUp) {
            with(bottom_nav_bar, {
                visibility = View.VISIBLE
                animate()
                        .translationY(0f)
                        .alpha(1f)
                        .withEndAction {
                            bottomNavUp = true
                        }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.savedInstanceState = outState
    }

    private fun setupReceivers() {
        this.wifiReceiver = WifiReceiver()
        var filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(this.wifiReceiver, filter)

        this.playReceiver = PlayReceiver(this)
        filter = IntentFilter()
        filter.addAction(Constants.Action.Play.PLAY_ACTION)
        filter.addAction(Constants.Action.Play.PLAY_NEXT_ACTION)
        filter.addAction(Constants.Action.Play.PLAY_NOW_ACTION)
        filter.addAction(Constants.Action.Play.PLAY_ALL_ACTION)
        filter.addAction(Constants.Action.Play.SHUFFLE_ACTION)
        registerReceiver(this.playReceiver, filter)
    }

    private fun setupPlayerFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        when {
            this.playerFragment != null -> {
                transaction.show(this.playerFragment!!)
                this.refreshVideoFavorited()
            }
            else -> {
                this.playerFragment = supportFragmentManager.findFragmentByTag(PlayerFragment.TAG) as PlayerFragment?
                if (this.playerFragment != null) {
                    transaction.show(this.playerFragment!!)
                    this.refreshVideoFavorited()
                } else {
                    this.playerFragment = PlayerFragment()
                    transaction.add(R.id.player_container, this.playerFragment!!, PlayerFragment.TAG)
                }
            }
        }
        transaction.commitNow()
    }

    private fun switchToHomeFragment() {
        if (this.currentFragment != Constants.Fragment.HOME) {
            val transaction = supportFragmentManager.beginTransaction()
            when {
                this.homeFragment != null -> transaction.show(this.homeFragment!!)
                else -> {
                    this.homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment?
                    if (this.homeFragment != null) {
                        transaction.show(this.homeFragment!!)
                    } else {
                        this.homeFragment = HomeFragment()
                        this.homeFragment!!.onPlayItemsListener = this
                        transaction.add(R.id.content_frame, this.homeFragment!!, HomeFragment.TAG)
                    }
                }
            }
            this.searchFragment?.let { transaction.hide(this.searchFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.HOME
            this.enableScroll()
        }
    }

    private fun switchToSearchFragment() {
        if (this.currentFragment != Constants.Fragment.SEARCH) {
            val transaction = supportFragmentManager.beginTransaction()
            when {
                this.searchFragment != null -> transaction.show(this.searchFragment!!)
                else -> {
                    this.searchFragment = supportFragmentManager.findFragmentByTag(SearchFragment.TAG) as SearchFragment?
                    if (this.searchFragment != null) {
                        transaction.show(this.searchFragment!!)
                    } else {
                        this.searchFragment = SearchFragment()
                        this.searchFragment!!.onPlayItemsListener = this
                        transaction.add(R.id.content_frame, this.searchFragment!!, SearchFragment.TAG)
                    }
                }
            }
            this.homeFragment?.let { transaction.hide(this.homeFragment!!) }
            this.libraryFragment?.let { transaction.hide(this.libraryFragment!!) }
            transaction.commitNow()
            this.currentFragment = Constants.Fragment.SEARCH
            this.disableScroll()
        }
    }

    private fun switchToLibraryFragment() {
        if (this.currentFragment != Constants.Fragment.LIBRARY) {
            val transaction = supportFragmentManager.beginTransaction()
            when {
                this.libraryFragment != null -> transaction.show(this.libraryFragment!!)
                else -> {
                    this.libraryFragment = supportFragmentManager.findFragmentByTag(LibraryFragment.TAG) as LibraryFragment?
                    if (this.libraryFragment != null) {
                        transaction.show(this.libraryFragment!!)
                    } else {
                        this.libraryFragment = LibraryFragment()
                        this.libraryFragment!!.onPlayItemsListener = this
                        transaction.add(R.id.content_frame, this.libraryFragment!!, LibraryFragment.TAG)
                    }
                }
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

        GsonPlaylistHelper.create(this)

        this.currentFragment = -1

        startService(Intent(this, KillNotificationService::class.java))

        setupReceivers()
        setSupportActionBar(toolbar)
        bottom_nav_bar.setOnNavigationItemSelectedListener(this)
        switchToHomeFragment()
        setupPlayerFragment()

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val action = intent.action
            if (action == Intent.ACTION_SEND) {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val pattern = Pattern.compile(YOUTUBE_REGEX)
                val matcher = pattern.matcher(text)
                if (matcher.find()) {
                    val videoId = matcher.group(2)
                    if (videoId.length == 11) {
                        var video = Video(videoId)
                        video = YouTubeService.get().requestDetails(video)
                        onPlay(video)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_rate -> {
                this.launchMarket()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchMarket() {
        var uri = Uri.parse("market://details?id=$packageName")
        val action = Intent.ACTION_VIEW
        val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var marketIntent: Intent
        try {
            marketIntent = Intent(action, uri)
            marketIntent.setPackage("com.android.vending")
            marketIntent.flags = flags
            startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            marketIntent = Intent(action, uri)
            marketIntent.flags = flags
            startActivity(marketIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onBackPressed() {
        if (!this.playerFragment!!.swipeDown()) {
            bottom_nav_bar.menu.findItem(R.id.nav_item_home).isChecked = true
            this.switchToHomeFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(this.playReceiver)
        unregisterReceiver(this.wifiReceiver)
    }

    override fun onPlayAll(videos: Collection<Video>) {
        if (playerFragment != null && videos.isNotEmpty() && playerFragment!!.setQueueTo(videos)) {
            playerFragment!!.forcePlayNext()
        }
    }

    override fun onPlay(video: Video) {
        if (playerFragment != null && playerFragment!!.addToQueue(video)) {
            playerFragment!!.tryPlayNext()
        }
    }

    override fun onShuffle(videos: Collection<Video>) {
        onPlayAll(videos.shuffled())
    }

    override fun onPlayNext(video: Video) {
        if (playerFragment != null && playerFragment!!.addToQueue(0, video)) {
            playerFragment!!.tryPlayNext()
        }
    }

    override fun onPlayNow(video: Video) {
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

        const val YOUTUBE_REGEX = """^(?:(?:https?:\/\/)?(?:www\.)?)?(youtube(?:-nocookie)?\.com|youtu\.be)\/.*?(?:embed|e|v|watch\?.*?v=)?\/?([a-zA-Z0-9]+)"""

    }

}
