package com.arman.queuetube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.arman.queuetube.R;
import com.arman.queuetube.fragments.ListFragment;
import com.arman.queuetube.fragments.MainFragment;
import com.arman.queuetube.listeners.DrawerItemListener;
import com.arman.queuetube.util.services.KillNotificationService;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public static final int MAIN_FRAGMENT = 0;
    public static final int LIST_FRAGMENT = 1;

    private DrawerLayout drawerLayout;

    private MainFragment mainFragment;
    private ListFragment listFragment;

    private int currentFragment;

    private void setupDrawer() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = this.drawerLayout.findViewById(R.id.drawer_navigation_view);
        navigationView.setCheckedItem(R.id.menu_item_home);
        navigationView.setNavigationItemSelectedListener(new DrawerItemListener(this, this.drawerLayout));
    }

    public void switchToMainFragment() {
        if (this.currentFragment != MAIN_FRAGMENT) {
            if (this.mainFragment != null) {
                getSupportFragmentManager().beginTransaction().show(this.mainFragment).commit();
            } else {
                this.mainFragment = new MainFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_frame, this.mainFragment).commit();
            }
            if (this.listFragment != null) {
                getSupportFragmentManager().beginTransaction().hide(this.listFragment).commit();
            }
            this.currentFragment = MAIN_FRAGMENT;
        }
    }

    public void switchToListFragment() {
        if (this.currentFragment != LIST_FRAGMENT) {
            if (this.listFragment != null) {
                getSupportFragmentManager().beginTransaction().show(this.listFragment).commit();
            } else {
                this.listFragment = new ListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment_frame, this.listFragment).commit();
            }
            if (this.mainFragment != null) {
                getSupportFragmentManager().beginTransaction().hide(this.mainFragment).commit();
            }
            this.currentFragment = LIST_FRAGMENT;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.currentFragment = -1;

        startService(new Intent(this, KillNotificationService.class));

        switchToMainFragment();
        setupDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView view = (SearchView) item.getActionView();
        this.mainFragment.addSearchListeners(view);
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
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawers();
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

}
