package com.arman.queuetube.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class WifiReceiver : BroadcastReceiver() {

    private var snackbar: Snackbar? = null

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val activity = context as Activity
            val container = activity.container
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
