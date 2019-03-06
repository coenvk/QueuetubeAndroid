package com.arman.queuetube.applications;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class BootstrapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }

}
