package com.arman.queuetube.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.arman.queuetube.util.itemtouchhelper.VideoItemTouchHelper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

public class PlaylistFragment extends DefaultPlaylistFragment {

    public static final String TAG = "PlaylistFragment";

    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemTouchHelper.Callback callback = new VideoItemTouchHelper.Callback(this.playlistAdapter);
        this.itemTouchHelper = new ItemTouchHelper(callback);
        this.itemTouchHelper.attachToRecyclerView(this.playlistView);
    }

}
