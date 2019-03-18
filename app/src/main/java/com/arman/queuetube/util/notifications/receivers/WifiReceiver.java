package com.arman.queuetube.util.notifications.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.arman.queuetube.R;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Activity activity = (Activity) context;
            if (isOnline(context)) {
                activity.findViewById(R.id.error_message_container).setVisibility(View.GONE);
                activity.findViewById(R.id.ad_view).setVisibility(View.VISIBLE);
            } else {
                activity.findViewById(R.id.ad_view).setVisibility(View.GONE);
                activity.findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

}
