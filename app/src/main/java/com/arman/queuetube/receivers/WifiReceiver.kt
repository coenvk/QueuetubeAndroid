package com.arman.queuetube.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.arman.queuetube.R

class WifiReceiver : BroadcastReceiver() {

    private var flashbar: Flashbar? = null

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val activity = context as? Activity
            activity?.let {
                if (isOnline(context)) {
                    this.flashbar?.dismiss()
                } else {
                    this.flashbar = Flashbar.Builder(it)
                            .gravity(Flashbar.Gravity.TOP)
                            .title("No connection!")
                            .message("You have lost connection to the internet")
                            .backgroundColorRes(R.color.colorPrimaryDark)
                            .showIcon()
                            .icon(R.drawable.wifi_strength_off)
                            .iconColorFilterRes(android.R.color.white)
                            .iconAnimation(
                                    FlashAnim.with(it)
                                            .animateIcon()
                                            .pulse()
                                            .alpha()
                                            .duration(750)
                                            .accelerate()
                            )
                            .enableSwipeToDismiss()
                            .build()
                    this.flashbar!!.show()
                }
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
