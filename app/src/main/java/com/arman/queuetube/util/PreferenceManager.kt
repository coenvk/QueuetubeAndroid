package com.arman.queuetube.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    var isFirstLaunch: Boolean
        get() = this.sharedPreferences.getBoolean(FIRST_LAUNCH, true)
        set(isFirstLaunch) {
            this.editor.putBoolean(FIRST_LAUNCH, isFirstLaunch)
            this.editor.commit()
        }

    init {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE, MODE)
        this.editor = this.sharedPreferences.edit()
    }

    companion object {

        const val FIRST_LAUNCH = "FIRST_LAUNCH"
        const val PREFERENCE = "Queuetube"
        const val MODE = 0
    }

}
