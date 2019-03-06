package com.arman.queuetube.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.PlayerFragment;
import com.arman.queuetube.fragments.pager.ViewPagerAdapter;
import com.arman.queuetube.listeners.DrawerItemListener;
import com.arman.queuetube.modules.search.SearchListener;
import com.arman.queuetube.util.notifications.receivers.NotificationReceiver;
import com.arman.queuetube.util.services.KillNotificationService;
import com.arman.queuetube.util.transformers.DepthPageTransformer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int RECOVERY_DIALOG_REQUEST = 1;

    private DrawerLayout drawerLayout;

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private BroadcastReceiver broadcastReceiver;

    private void setupReceiver() {
        this.broadcastReceiver = new NotificationReceiver((PlayerFragment) this.pagerAdapter.getPlayerFragment());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Action.NEXT_ACTION);
        filter.addAction(Constants.Action.MAIN_ACTION);
        filter.addAction(Constants.Action.PAUSE_ACTION);
        filter.addAction(Constants.Action.PLAY_ACTION);
        filter.addAction(Constants.Action.STOP_ACTION);
        registerReceiver(this.broadcastReceiver, filter);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void setupDrawer() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = this.drawerLayout.findViewById(R.id.drawer_navigation_view);
        navigationView.setCheckedItem(R.id.menu_item_home);
        navigationView.setNavigationItemSelectedListener(new DrawerItemListener(this, this.drawerLayout));
    }

    private void setupAdView() {
        MobileAds.initialize(this, Constants.Key.TEST_AD_KEY);
        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setupPager() {
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(this.viewPager);

        this.pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, KillNotificationService.class));

        setupPager();
        setupReceiver();

        setupActionBar();
        setupDrawer();
        setupAdView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView view = (SearchView) item.getActionView();
        view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.viewPager.setCurrentItem(ViewPagerAdapter.SEARCH_INDEX);
            }
        });
        view.setOnQueryTextListener(new SearchListener((PlayerFragment) this.pagerAdapter.getPlayerFragment()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (this.viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1);
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_DIALOG_REQUEST) {
//            getYouTubePlayerProvider().initialize(API_KEY, this);
//        }
//    }
//
//    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerView) findViewById(R.id.youtube_player);
//    }

}
