package com.arman.queuetube.activities;

import android.os.Bundle;
import android.view.View;

import com.arman.queuetube.R;
import com.arman.queuetube.fragments.ListFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PlaylistActivity extends AppCompatActivity {

    private ListFragment listFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playlist);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.playlist_frame, this.listFragment).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
