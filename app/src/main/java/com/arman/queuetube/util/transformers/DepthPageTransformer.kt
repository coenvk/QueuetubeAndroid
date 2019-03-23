package com.arman.queuetube.util.transformers

import android.view.View

import androidx.viewpager.widget.ViewPager

class DepthPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val w = page.width
        if (position < -1) {
            page.isClickable = false
            page.alpha = 0f
        } else if (position <= 0) {
            page.isClickable = true
            page.alpha = 1f
            page.translationX = 0f
            page.scaleX = 1f
            page.scaleY = 1f
        } else if (position <= 1) {
            page.alpha = 1 - position
            page.translationX = w * -position
            val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        } else {
            page.isClickable = false
            page.alpha = 0f
        }
    }

    companion object {

        private const val MIN_SCALE = .75f
    }

}
