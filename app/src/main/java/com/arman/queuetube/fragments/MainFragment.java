package com.arman.queuetube.fragments;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.fragments.pager.ViewPagerAdapter;
import com.arman.queuetube.modules.search.SearchListener;
import com.arman.queuetube.util.notifications.receivers.NotificationReceiver;
import com.arman.queuetube.util.transformers.DepthPageTransformer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setupActionBar(view);
        this.setupPager(view);
        this.setupAdView(view);
        this.setupReceiver();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    private void setupActionBar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void setupAdView(View view) {
        MobileAds.initialize(getActivity(), Constants.Key.TEST_AD_KEY);
        AdView adView = (AdView) getActivity().findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setupPager(View view) {
        this.viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(this.viewPager);

        this.pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    public void addSearchListeners(SearchView view) {
        view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment.this.viewPager.setCurrentItem(ViewPagerAdapter.SEARCH_INDEX);
            }
        });
        view.setOnQueryTextListener(new SearchListener((PlayerFragment) this.pagerAdapter.getPlayerFragment()));
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
