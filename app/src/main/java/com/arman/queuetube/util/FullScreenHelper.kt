package com.arman.queuetube.util

import android.app.Activity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class FullScreenHelper(private val activity: Activity) {

    private var views: MutableList<View?> = ArrayList()

    fun addViews(vararg views: View): Boolean {
        return this.views.addAll(views)
    }

    fun addView(view: View): Boolean {
        return this.views.add(view)
    }

    fun removeView(view: View): Boolean {
        return this.views.remove(view)
    }

    fun enterFullScreen() {
        val decorView = activity.window.decorView
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        views.forEach {
            it?.let { that ->
                that.visibility = View.GONE
                that.invalidate()
            }
        }

        activity.bottom_nav_bar?.let {
            it.animate()
                    .alpha(0f)
                    .withEndAction { it.visibility = View.GONE }
        }
    }

    fun exitFullScreen() {
        val decorView = activity.window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        views.forEach {
            it?.let { that ->
                that.visibility = View.VISIBLE
                that.invalidate()
            }
        }

        activity.bottom_nav_bar?.let {
            it.visibility = View.VISIBLE
            it.animate()
                    .alpha(1f)
        }
    }

}