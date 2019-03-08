package com.arman.queuetube.listeners;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

import com.arman.queuetube.R;
import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.activities.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DrawerItemListener implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MainActivity activity;

    public DrawerItemListener(MainActivity activity, DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (item.isCheckable()) {
            item.setChecked(true);

            switch (id) {
                case R.id.menu_item_home:
                    activity.switchToMainFragment();
                    break;
                case R.id.menu_item_playlists:
                    activity.switchToListFragment();
                    break;
            }

            this.drawerLayout.closeDrawers();
            return true;
        }

        switch (id) {
            case R.id.menu_item_settings:
                this.activity.startActivity(new Intent(activity, SettingsActivity.class));
                return true;
            case R.id.menu_item_rate:
                this.launchMarket();
                return true;
            case R.id.menu_item_buy_pro:
                return true;
            case R.id.menu_item_info:
                return true;
            default:
                return false;
        }
    }

    private void launchMarket() {
        String appPackageName = activity.getPackageName();
        Uri uri = Uri.parse("market://details?id=" + appPackageName);
        String action = Intent.ACTION_VIEW;
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP;
        Intent marketIntent;
        try {
            marketIntent = new Intent(action, uri);
            marketIntent.setPackage("com.android.vending");
            marketIntent.setFlags(flags);
            activity.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
            marketIntent = new Intent(action, uri);
            marketIntent.setFlags(flags);
            activity.startActivity(marketIntent);
        }
    }

}
