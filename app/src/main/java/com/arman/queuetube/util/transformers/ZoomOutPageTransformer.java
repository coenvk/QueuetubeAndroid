package com.arman.queuetube.util.transformers;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = .85f;
    private static final float MIN_ALPHA = .5f;

    @Override
    public void transformPage(View page, float position) {
        int w = page.getWidth();
        int h = page.getHeight();
        if (position < -1) {
            page.setAlpha(0f);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vMargin = h * (1 - scaleFactor) / 2;
            float hMargin = w * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(hMargin - vMargin / 2);
            } else {
                page.setTranslationX(-hMargin + vMargin / 2);
            }
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else {
            page.setAlpha(0f);
        }
    }

}
