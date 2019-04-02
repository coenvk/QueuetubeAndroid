package com.arman.queuetube.util.notifications.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import com.arman.queuetube.R
import com.google.android.material.snackbar.Snackbar

class WifiReceiver : BroadcastReceiver() {

    private var snackbar: Snackbar? = null

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val activity = context as Activity
            val container = activity.findViewById<View>(R.id.container)
            if (isOnline(context)) {
                this.snackbar?.dismiss()
            } else {
                this.snackbar = Snackbar.make(container, "You don't have an internet connection", Snackbar.LENGTH_INDEFINITE)
                this.snackbar!!.show()
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
