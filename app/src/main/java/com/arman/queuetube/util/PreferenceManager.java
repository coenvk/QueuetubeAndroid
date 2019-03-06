package com.arman.queuetube.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    public static final String FIRST_LAUNCH = "FIRST_LAUNCH";
    public static final String PREFERENCE = "Queuetube";
    public static final int MODE = 0;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE, MODE);
        this.editor = this.sharedPreferences.edit();
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        this.editor.putBoolean(FIRST_LAUNCH, isFirstLaunch);
        this.editor.commit();
    }

    public boolean isFirstLaunch() {
        return this.sharedPreferences.getBoolean(FIRST_LAUNCH, true);
    }

}
