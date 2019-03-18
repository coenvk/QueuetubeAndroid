package com.arman.queuetube.fragments;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.pager.ViewPagerAdapter;
import com.arman.queuetube.util.notifications.receivers.NotificationReceiver;
import com.arman.queuetube.util.transformers.DepthPageTransformer;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MainFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setupPager(view);
        this.setupReceiver();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView view = (SearchView) item.getActionView();
        this.addSearchListeners(view);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public ViewPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    private void setupPager(View view) {
        this.viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(this.viewPager);

        this.pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void addSearchListeners(SearchView view) {
        view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment.this.viewPager.setCurrentItem(ViewPagerAdapter.SEARCH_INDEX);
            }
        });
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                MainFragment.this.viewPager.setCurrentItem(ViewPagerAdapter.SEARCH_INDEX);
                PlayerFragment playerFragment = (PlayerFragment) MainFragment.this.pagerAdapter.getPlayerFragment();
                playerFragment.query(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void setupReceiver() {
        this.broadcastReceiver = new NotificationReceiver((PlayerFragment) this.pagerAdapter.getPlayerFragment());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Action.NEXT_ACTION);
        filter.addAction(Constants.Action.MAIN_ACTION);
        filter.addAction(Constants.Action.PAUSE_ACTION);
        filter.addAction(Constants.Action.PLAY_ACTION);
        filter.addAction(Constants.Action.STOP_ACTION);
        getActivity().registerReceiver(this.broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.broadcastReceiver);
    }

}
