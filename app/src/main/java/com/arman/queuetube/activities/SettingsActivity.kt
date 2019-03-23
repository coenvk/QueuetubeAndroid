package com.arman.queuetube.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.arman.queuetube.R
import com.arman.queuetube.util.notifications.NotificationHelper

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 *
 * See [
 * Android Design: Settings](http://developer.android.com/design/patterns/settings.html) for design guidelines and the [Settings
 * API Guide](http://developer.android.com/guide/topics/ui/settings.html) for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName || SettingsFragment::class.java.name == fragmentName
    }

    class SettingsFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_main)
            setHasOptionsMenu(true)

            var preference = findPreference(getString(R.string.enable_notifications_key))
            preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                NotificationHelper.destroyNotification(context)
                true
            }

            preference = findPreference(getString(R.string.send_feedback_key))
            preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                sendFeedback(activity)
                true
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                android.R.id.home -> {
                    startActivity(Intent(activity, SettingsActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    }

    companion object {

        val TAG = "SettingsActivity"

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        fun sendFeedback(context: Context) {
            var body: String? = null
            try {
                body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
                body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER
            } catch (e: PackageManager.NameNotFoundException) {
            }

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("c.m.vankampen@live.nl"))
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name) + " issue")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)))
        }
    }

}
