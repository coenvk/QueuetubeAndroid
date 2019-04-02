package com.arman.queuetube.model

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageLoader(private val context: Context) {

    fun load(url: String?, view: ImageView) {
        if (url != null && url.isNotEmpty()) {
            Glide
                    .with(context)
                    .load(url)
                    .placeholder(ColorDrawable(Color.GRAY))
                    .error(ColorDrawable(Color.GRAY))
                    .centerCrop()
                    .into(view)
        }
    }

}