package com.arman.queuetube.util.transformers;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class DepthPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = .75f;

    @Override
    public void transformPage(View page, float position) {
        int w = page.getWidth();
        if (position < -1) {
            page.setClickable(false);
            page.setAlpha(0f);
        } else if (position <= 0) {
            page.setClickable(true);
            page.setAlpha(1f);
            page.setTranslationX(0f);
            page.setScaleX(1f);
            page.setScaleY(1f);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
            page.setTranslationX(w * -position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {
            page.setClickable(false);
            page.setAlpha(0f);
        }
    }

}
