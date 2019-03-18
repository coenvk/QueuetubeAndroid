package com.arman.queuetube.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.DefaultPlaylistFragment;
import com.arman.queuetube.fragments.MainFragment;
import com.arman.queuetube.fragments.PlayerFragment;
import com.arman.queuetube.fragments.PlaylistFragment;
import com.arman.queuetube.fragments.PlaylistsFragment;
import com.arman.queuetube.fragments.StreamFragment;
import com.arman.queuetube.listeners.DrawerItemListener;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.modules.playlists.PlaylistHelper;
import com.arman.queuetube.util.notifications.receivers.WifiReceiver;
import com.arman.queuetube.util.services.KillNotificationService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public static final int MAIN_FRAGMENT = 0;
    public static final int STREAM_FRAGMENT = 1;
    public static final int FAVORITES_FRAGMENT = 2;
    public static final int HISTORY_FRAGMENT = 3;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout extendedToolbar;

    private MainFragment mainFragment;
    private DefaultPlaylistFragment favoritesFragment;
    private DefaultPlaylistFragment historyFragment;
    private StreamFragment streamFragment;
    private PlaylistsFragment playlistsFragment;

    private WifiReceiver wifiReceiver;

    private int currentFragment;

    private void setupWifiReceiver() {
        this.wifiReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.wifiReceiver, filter);
    }

    private void setupDrawer() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = this.drawerLayout.findViewById(R.id.drawer_navigation_view);
        navigationView.setCheckedItem(R.id.menu_item_home);
        DrawerItemListener drawerListener = new DrawerItemListener(this, this.drawerLayout);
        navigationView.setNavigationItemSelectedListener(drawerListener);
        this.drawerLayout.addDrawerListener(drawerListener);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        this.setupDrawerToggle(toolbar);
    }

    private void setupDrawerToggle(Toolbar toolbar) {
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        this.drawerLayout.addDrawerListener(this.drawerToggle);
    }

    private void setupAdView() {
        MobileAds.initialize(this, Constants.Key.TEST_AD_KEY);
        AdView adView = (AdView) this.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setupPlaylistFragment(int index) {
        Bundle bundle = new Bundle();
        switch (index) {
            case FAVORITES_FRAGMENT:
                bundle.putString("playlistName", PlaylistHelper.FAVORITES);
                this.favoritesFragment = new PlaylistFragment();
                this.favoritesFragment.setArguments(bundle);
                break;
            case HISTORY_FRAGMENT:
                bundle.putString("playlistName", PlaylistHelper.HISTORY);
                this.historyFragment = new DefaultPlaylistFragment();
                this.historyFragment.setArguments(bundle);
                break;
            default:
                break;
        }
    }

    private void setupFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        this.setupPlaylistFragment(FAVORITES_FRAGMENT);
        transaction.replace(R.id.favorites_fragment_frame, this.favoritesFragment).hide(this.favoritesFragment);

        this.setupPlaylistFragment(HISTORY_FRAGMENT);
        transaction.replace(R.id.history_fragment_frame, this.historyFragment).hide(this.historyFragment);

        this.streamFragment = new StreamFragment();
        transaction.replace(R.id.stream_fragment_frame, this.streamFragment).hide(this.streamFragment);

        transaction.commit();

        this.switchToMainFragment();
    }

    public void switchToMainFragment() {
        if (this.currentFragment != MAIN_FRAGMENT) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (this.mainFragment != null) {
                transaction.show(this.mainFragment);
                this.refreshVideoFavorited();
            } else {
                this.mainFragment = new MainFragment();
                transaction.replace(R.id.main_fragment_frame, this.mainFragment);
            }
            if (this.favoritesFragment != null) {
                transaction.hide(this.favoritesFragment);
            }
            if (this.historyFragment != null) {
                transaction.hide(this.historyFragment);
            }
            if (this.streamFragment != null) {
                transaction.hide(this.streamFragment);
            }
            if (this.extendedToolbar != null) {
                this.extendedToolbar.setVisibility(View.VISIBLE);
            }
            transaction.commitNow();
            this.currentFragment = MAIN_FRAGMENT;
        }
    }

    public void switchToFavoritesFragment() {
        if (this.currentFragment != FAVORITES_FRAGMENT) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (this.favoritesFragment != null) {
                transaction.show(this.favoritesFragment);
            } else {
                this.setupPlaylistFragment(FAVORITES_FRAGMENT);
                transaction.replace(R.id.favorites_fragment_frame, this.favoritesFragment);
            }
            if (this.mainFragment != null) {
                transaction.hide(this.mainFragment);
            }
            if (this.historyFragment != null) {
                transaction.hide(this.historyFragment);
            }
            if (this.streamFragment != null) {
                transaction.hide(this.streamFragment);
            }
            if (this.extendedToolbar != null) {
                this.extendedToolbar.setVisibility(View.GONE);
            }
            transaction.commitNow();
            this.currentFragment = FAVORITES_FRAGMENT;
        }
    }

    public void switchToHistoryFragment() {
        if (this.currentFragment != HISTORY_FRAGMENT) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (this.historyFragment != null) {
                transaction.show(this.historyFragment);
            } else {
                this.setupPlaylistFragment(HISTORY_FRAGMENT);
                transaction.replace(R.id.history_fragment_frame, this.historyFragment);
            }
            if (this.mainFragment != null) {
                transaction.hide(this.mainFragment);
            }
            if (this.favoritesFragment != null) {
                transaction.hide(this.favoritesFragment);
            }
            if (this.streamFragment != null) {
                transaction.hide(this.streamFragment);
            }
            if (this.extendedToolbar != null) {
                this.extendedToolbar.setVisibility(View.GONE);
            }
            transaction.commitNow();
            this.currentFragment = HISTORY_FRAGMENT;
        }
    }

    public void switchToStreamFragment() {
        if (this.currentFragment != STREAM_FRAGMENT) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (this.streamFragment != null) {
                transaction.show(this.streamFragment);
            } else {
                this.streamFragment = new StreamFragment();
                transaction.replace(R.id.stream_fragment_frame, this.streamFragment);
            }
            if (this.mainFragment != null) {
                transaction.hide(this.mainFragment);
            }
            if (this.favoritesFragment != null) {
                transaction.hide(this.favoritesFragment);
            }
            if (this.historyFragment != null) {
                transaction.hide(this.historyFragment);
            }
            if (this.extendedToolbar != null) {
                this.extendedToolbar.setVisibility(View.GONE);
            }
            transaction.commitNow();
            this.currentFragment = STREAM_FRAGMENT;
        }
    }

    public boolean setQueueTo(List<VideoData> videos) {
        PlayerFragment playerFragment = (PlayerFragment) this.mainFragment.getPagerAdapter().getPlayerFragment();
        boolean ret = playerFragment.setQueueTo(videos);
        playerFragment.forcePlayNext();
        return ret;
    }

    public boolean addToQueue(VideoData item) {
        PlayerFragment playerFragment = (PlayerFragment) this.mainFragment.getPagerAdapter().getPlayerFragment();
        boolean ret = playerFragment.addToQueue(item);
        playerFragment.tryPlayNext();
        return ret;
    }

    public void refreshVideoFavorited() {
        PlayerFragment playerFragment = (PlayerFragment) this.mainFragment.getPagerAdapter().getPlayerFragment();
        VideoData currentVideo = playerFragment.getCurrentVideo();
        playerFragment.updateVideo(PlaylistHelper.isFavorited(currentVideo));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaylistHelper.onCreate(this);

        this.currentFragment = -1;

        startService(new Intent(this, KillNotificationService.class));

        this.extendedToolbar = (LinearLayout) findViewById(R.id.main_extended_toolbar);

        setupWifiReceiver();
        setupDrawer();
        setupAdView();
        setupActionBar();
        setupFragments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (this.currentFragment == MAIN_FRAGMENT) {
            ViewPager viewPager = this.mainFragment.getViewPager();
            if (viewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        } else {
            NavigationView navigationView = (NavigationView) this.drawerLayout.findViewById(R.id.drawer_navigation_view);
            navigationView.getMenu().findItem(R.id.menu_item_home).setChecked(true);
            this.switchToMainFragment();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.wifiReceiver);
    }

    public void refreshPlaylists() {
        this.favoritesFragment.loadPlaylist();
        this.historyFragment.loadPlaylist();
    }

}
