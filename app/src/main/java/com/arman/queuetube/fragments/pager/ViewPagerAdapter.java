package com.arman.queuetube.fragments.pager;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.arman.queuetube.R;
import com.arman.queuetube.fragments.PlayerFragment;
import com.arman.queuetube.fragments.QueueFragment;
import com.arman.queuetube.fragments.SearchFragment;
import com.arman.queuetube.model.adapters.BaseTouchAdapter;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public static final String TAG = "ViewPagerAdapter";

    public static final int NUM_PAGES = 2;

    public static final int QUEUE_INDEX = 0;
    public static final int SEARCH_INDEX = 1;
    public static final String[] PAGE_TITLES = {"Queue", "Search"};

    private Fragment playerFragment;

    private List<Fragment> fragments;
    private List<String> fragmentTitles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new LinkedList<>();
        this.fragmentTitles = new LinkedList<>();

        this.addFragment(new QueueFragment());
        this.addFragment(new SearchFragment());

        this.setupPlayer(fm);
    }

    public void setupPlayer(FragmentManager fm) {
        this.playerFragment = new PlayerFragment();
        fm.beginTransaction().replace(R.id.top_player, this.playerFragment).commit();
    }

    public Fragment getPlayerFragment() {
        return playerFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position % getCount());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.fragmentTitles.get(position % getCount());
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    public Fragment getFragmentById(@IdRes int id) {
        for (Fragment fragment : this.fragments) {
            if (fragment.getId() == id) {
                return fragment;
            }
        }
        return null;
    }

    public Fragment getFragmentByIndex(int index) {
        return this.fragments.get(index);
    }

    public Fragment getFragmentByPageTitle(String title) {
        int index = this.fragmentTitles.indexOf(title);
        if (index >= 0 && index < this.fragments.size()) {
            return this.fragments.get(index);
        }
        return null;
    }

    public void addFragment(Fragment fragment) {
        if (fragment instanceof QueueFragment) {
            this.fragmentTitles.add(PAGE_TITLES[QUEUE_INDEX]);
            ((QueueFragment) fragment).setOnItemClickListener(new QueueItemClickListener());
        } else if (fragment instanceof SearchFragment) {
            this.fragmentTitles.add(PAGE_TITLES[SEARCH_INDEX]);
            ((SearchFragment) fragment).setOnItemClickListener(new SearchItemClickListener());
        }
        this.fragments.add(fragment);
    }

    public class QueueItemClickListener implements BaseTouchAdapter.OnItemClickListener {

        @Override
        public void onClick(final RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setClickable(false);
            final QueueFragment queueFragment = (QueueFragment) ViewPagerAdapter.this.getFragmentByIndex(QUEUE_INDEX);

            Animation animation = AnimationUtils.loadAnimation(queueFragment.getActivity(), R.anim.remove_from_queue);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    queueFragment.getQueueAdapter().remove(viewHolder.getAdapterPosition());
                    if (queueFragment.getQueueAdapter().isEmpty()) {
                        queueFragment.showEmptyText();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            viewHolder.itemView.startAnimation(animation);
        }

    }

    public class SearchItemClickListener implements BaseTouchAdapter.OnItemClickListener {

        @Override
        public void onClick(final RecyclerView.ViewHolder viewHolder) {
            final QueueFragment queueFragment = (QueueFragment) ViewPagerAdapter.this.getFragmentByIndex(QUEUE_INDEX);
            final SearchFragment searchFragment = (SearchFragment) ViewPagerAdapter.this.getFragmentByIndex(SEARCH_INDEX);

            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.add_from_search_results);
            viewHolder.itemView.startAnimation(animation);

            queueFragment.getQueueAdapter().add(searchFragment.getResultsAdapter().get(viewHolder.getAdapterPosition()));
            ((PlayerFragment) ViewPagerAdapter.this.playerFragment).tryPlayNext();
        }

    }

}
