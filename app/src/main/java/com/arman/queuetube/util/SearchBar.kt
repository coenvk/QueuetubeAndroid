package com.arman.queuetube.util

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.SearchView


class SearchBar : SearchView {

    constructor(context: Context) : super(context) {
        onCreate()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        onCreate()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        onCreate()
    }

    fun onCreate() {
        val searchEditFrameId = resources.getIdentifier("android:id/search_edit_frame", null, null)
        val searchEditFrame = findViewById<View>(searchEditFrameId)
        searchEditFrame?.setBackgroundColor(Color.WHITE)
        val searchSrcTextId = resources.getIdentifier("android:id/search_src_text", null, null)
        val searchSrcText = findViewById<EditText>(searchSrcTextId)
        searchSrcText?.let {
            it.setBackgroundResource(android.R.color.transparent)
            it.setTextColor(Color.BLACK)
            it.setHintTextColor(Color.GRAY)
        }
    }

}