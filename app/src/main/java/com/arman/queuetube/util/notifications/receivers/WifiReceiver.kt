package com.arman.queuetube.util.notifications.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import com.arman.queuetube.R

class WifiReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val activity = context as Activity
            if (isOnline(context)) {
                activity.findViewById<View>(R.id.error_message_container).visibility = View.GONE
            } else {
                activity.findViewById<View>(R.id.error_message_container).visibility = View.VISIBLE
            }
        } catch (e: Exception) {
        }

    }

    private fun isOnline(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return info != null && info.isConnected
    }

}
