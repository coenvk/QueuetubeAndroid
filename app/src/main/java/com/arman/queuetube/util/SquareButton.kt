package com.arman.queuetube.util

import android.content.Context
import android.util.AttributeSet
import android.widget.Button

class SquareButton : Button {

    private var squareDim = Integer.MAX_VALUE

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val h = measuredHeight
        val w = measuredWidth
        var squareDim = Math.min(w, h)

        if (squareDim == 0) {
            squareDim = Math.max(w, h)
        }

        if (squareDim < this.squareDim) {
            this.squareDim = squareDim
        }

        setMeasuredDimension(this.squareDim, this.squareDim)

    }

}