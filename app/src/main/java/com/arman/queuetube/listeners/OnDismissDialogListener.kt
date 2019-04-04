package com.arman.queuetube.listeners

import android.os.Bundle

inline fun OnDismissDialogListener(
        crossinline onDismissDialog: (Bundle) -> Unit = {}
): OnDismissDialogListener {
    return object : OnDismissDialogListener {
        override fun onDismissDialog(bundle: Bundle) {
            onDismissDialog(bundle)
        }
    }
}

interface OnDismissDialogListener {

    fun onDismissDialog(bundle: Bundle)

}