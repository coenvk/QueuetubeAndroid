package com.arman.queuetube.util

import android.content.Context
import android.content.SharedPreferences
import com.arman.queuetube.config.Constants

class IntroPreferenceManager {

    private val sharedPreferences: SharedPreferences
    private val context: Context

    constructor(context: Context) {
        this.context = context
        this.sharedPreferences = context.getSharedPreferences(Constants.Preferences.PREFERENCE, Context.MODE_PRIVATE)
    }

    fun setFirstLaunch(isFirstLaunch: Boolean) {
        val edit = this.sharedPreferences.edit()
        edit.putBoolean(Constants.Preferences.FIRST_LAUNCH, isFirstLaunch)
        edit.apply()
    }

    fun setFirstLaunch() {
        this.setFirstLaunch(this.isFirstLaunch())
    }

    fun isFirstLaunch(): Boolean {
        return this.sharedPreferences.getBoolean(Constants.Preferences.FIRST_LAUNCH, true)
    }

}