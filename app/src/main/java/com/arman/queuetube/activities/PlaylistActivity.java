package com.arman.queuetube.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.PlaylistFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupActionBar();

        String playlistName = getIntent().getStringExtra(Constants.Fragment.Argument.PLAYLIST_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, playlistName);
        PlaylistFragment fragment = new PlaylistFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
