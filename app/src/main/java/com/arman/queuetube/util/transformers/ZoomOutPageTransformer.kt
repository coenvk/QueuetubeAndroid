package com.arman.queuetube.util.transformers

import android.view.View

import androidx.viewpager.widget.ViewPager

class ZoomOutPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val w = page.width
        val h = page.height
        if (position < -1) {
            page.alpha = 0f
        } else if (position <= 1) {
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val vMargin = h * (1 - scaleFactor) / 2
            val hMargin = w * (1 - scaleFactor) / 2
            if (position < 0) {
                page.translationX = hMargin - vMargin / 2
            } else {
                page.translationX = -hMargin + vMargin / 2
            }
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
        } else {
            page.alpha = 0f
        }
    }

    companion object {

        private const val MIN_SCALE = .85f
        private const val MIN_ALPHA = .5f
    }

}
