package com.arman.queuetube.listeners

import android.os.Bundle

inline fun OnDialogDismissListener(
        crossinline onDialogDismiss: (Bundle) -> Unit = {}
): OnDialogDismissListener {
    return object : OnDialogDismissListener {
        override fun onDialogDismiss(bundle: Bundle) {
            onDialogDismiss(bundle)
        }
    }
}

interface OnDialogDismissListener {

    fun onDialogDismiss(bundle: Bundle)

}