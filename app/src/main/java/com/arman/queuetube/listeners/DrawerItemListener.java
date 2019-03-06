package com.arman.queuetube.listeners;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

import com.arman.queuetube.R;
import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.activities.PlaylistActivity;
import com.arman.queuetube.activities.SettingsActivity;
import com.arman.queuetube.modules.playlists.Playlist;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerItemListener implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Context context;

    public DrawerItemListener(Context context, DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
            this.drawerLayout.closeDrawers();
            return true;
        }
        int id = item.getItemId();
        switch(id) {
            case R.id.menu_item_settings:
                this.context.startActivity(new Intent(context, SettingsActivity.class));
                return true;
            case R.id.menu_item_rate:
                this.launchMarket();
                return true;
            case R.id.menu_item_home:
                return true;
            case R.id.menu_item_buy_pro:
                return true;
            case R.id.menu_item_info:
                return true;
            case R.id.playlists_menu_history:
                this.context.startActivity(new Intent(context, PlaylistActivity.class));
                return true;
            case R.id.playlists_menu_add_new:
                return true;
            case R.id.playlists_menu_favorites:
                this.context.startActivity(new Intent(context, PlaylistActivity.class));
                return true;
            default:
                return false;
        }
    }

    private void launchMarket() {
        String appPackageName = context.getPackageName();
        Uri uri = Uri.parse("market://details?id=" + appPackageName);
        String action = Intent.ACTION_VIEW;
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP;
        Intent marketIntent;
        try {
            marketIntent = new Intent(action, uri);
            marketIntent.setPackage("com.android.vending");
            marketIntent.setFlags(flags);
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
            marketIntent = new Intent(action, uri);
            marketIntent.setFlags(flags);
            context.startActivity(marketIntent);
        }
    }

}
