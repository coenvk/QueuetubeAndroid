package com.arman.queuetube.util

import android.content.Context
import android.content.Intent

import com.arman.queuetube.R
import com.arman.queuetube.model.VideoData

object VideoSharer {

    fun share(context: Context?, item: VideoData) {
        context?.let {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val body = "Shared with " + context.getString(R.string.app_name) + "\n\nhttps://youtu.be/" + item.id
            shareIntent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(Intent.createChooser(shareIntent, "Share video"))
        }
    }

}
