package com.arman.queuetube.activities.base

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar

/**
 * A [android.preference.PreferenceActivity] which implements and proxies the necessary calls
 * to be used with AppCompat.
 */
abstract class AppCompatPreferenceActivity : PreferenceActivity() {

    private var delegate: AppCompatDelegate? = null

    val supportActionBar: ActionBar?
        get() = getDelegate().supportActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        getDelegate().installViewFactory()
        getDelegate().onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        getDelegate().onPostCreate(savedInstanceState)
    }

    fun setSupportActionBar(toolbar: Toolbar?) {
        getDelegate().setSupportActionBar(toolbar)
    }

    override fun getMenuInflater(): MenuInflater {
        return getDelegate().menuInflater
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        getDelegate().setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        getDelegate().setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        getDelegate().setContentView(view, params)
    }

    override fun addContentView(view: View, params: ViewGroup.LayoutParams) {
        getDelegate().addContentView(view, params)
    }

    override fun onPostResume() {
        super.onPostResume()
        getDelegate().onPostResume()
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        getDelegate().setTitle(title)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        getDelegate().onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        super.onStop()
        getDelegate().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        getDelegate().onDestroy()
    }

    override fun invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu()
    }

    private fun getDelegate(): AppCompatDelegate {
        if (delegate == null) {
            delegate = AppCompatDelegate.create(this, null)
        }
        return delegate!!
    }

}
