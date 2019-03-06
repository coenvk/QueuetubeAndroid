package com.arman.queuetube.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.util.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class IntroActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private LinearLayout introSwitchBars;
    private TextView[] bottomBars;
    private int[] screens;
    private Button skipButton, nextButton;
    private ViewPager viewPager;
    private IntroViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        this.viewPager = (ViewPager) findViewById(R.id.intro_view_pager);
        this.introSwitchBars = (LinearLayout) findViewById(R.id.intro_switch_bars);
        this.skipButton = (Button) findViewById(R.id.intro_skip_button);
        this.nextButton = (Button) findViewById(R.id.intro_next_button);
        this.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip(view);
            }
        });
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });
        this.screens = new int[]{
                R.layout.intro_screen1,
                R.layout.intro_screen2,
                R.layout.intro_screen3
        };
        this.viewPagerAdapter = new IntroViewPagerAdapter();
        this.viewPager.setAdapter(this.viewPagerAdapter);
        this.preferenceManager = new PreferenceManager(this);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                colorBars(position);
                if (position == screens.length - 1) {
                    nextButton.setText("Start");
                    skipButton.setVisibility(View.GONE);
                } else {
                    nextButton.setText("Next");
                    skipButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (!this.preferenceManager.isFirstLaunch()) {
            this.launchMain();
            this.finish();
        }
        this.colorBars(0);
    }

    public void next(View view) {
        int i = getItem(+1);
        if (i < screens.length) {
            this.viewPager.setCurrentItem(i);
        } else {
            this.launchMain();
        }
    }

    public void skip(View view) {
        this.launchMain();
    }

    private void colorBars(int screen) {
        int[] colorsInactive = new int[]{0, 0, 0};
        int[] colorsActive = new int[]{255, 255, 255};
        this.bottomBars = new TextView[screens.length];
        this.introSwitchBars.removeAllViews();
        for (int i = 0; i < this.bottomBars.length; i++) {
            this.bottomBars[i] = new TextView(this);
            this.bottomBars[i].setTextSize(100);
            this.bottomBars[i].setText(Html.fromHtml("¯"));
            this.introSwitchBars.addView(this.bottomBars[i]);
            this.bottomBars[i].setTextColor(colorsInactive[screen]);
        }
        if (this.bottomBars.length > 0) {
            this.bottomBars[screen].setTextColor(colorsActive[screen]);
        }
    }

    private int getItem(int i) {
        return this.viewPager.getCurrentItem() + i;
    }

    private void launchMain() {
        this.preferenceManager.setFirstLaunch(false);
        this.startActivity(new Intent(IntroActivity.this, MainActivity.class));
        this.finish();
    }

    private class IntroViewPagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public IntroViewPagerAdapter() {

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }


}
