package com.arman.queuetube.modules.search;

import android.widget.SearchView;

import com.arman.queuetube.fragments.PlayerFragment;

public class SearchListener implements SearchView.OnQueryTextListener {

    private PlayerFragment playerFragment;

    public SearchListener(PlayerFragment playerFragment) {
        this.playerFragment = playerFragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.playerFragment.query(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
