package com.arman.queuetube.util;

import android.content.Context;
import android.content.Intent;

import com.arman.queuetube.R;
import com.arman.queuetube.fragments.PlayerFragment;
import com.arman.queuetube.model.VideoData;

public class VideoSharer {

    public static void share(Context context, VideoData item) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String body = "Shared with " + context.getString(R.string.app_name) + "\n\nhttps://youtu.be/" + item.getId();
        shareIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(shareIntent, "Share video"));
    }

}
