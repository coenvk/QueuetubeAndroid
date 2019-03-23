package com.arman.queuetube.applications

import android.app.Application

import com.beardedhen.androidbootstrap.TypefaceProvider

class BootstrapApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
    }

}
