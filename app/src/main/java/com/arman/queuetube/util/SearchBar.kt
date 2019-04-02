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
        val searchPlateId = resources.getIdentifier("android:id/search_src_text", null, null)
        val searchPlate = findViewById<EditText>(searchPlateId)
        searchPlate?.let {
            searchPlate.setBackgroundResource(android.R.color.transparent)
            searchPlate.setTextColor(Color.BLACK)
            searchPlate.setHintTextColor(Color.GRAY)
        }
    }

}