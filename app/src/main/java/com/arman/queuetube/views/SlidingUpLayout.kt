package com.arman.queuetube.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SlidingUpLayout : ViewGroup {

    private val dragHelper: ViewDragHelper = ViewDragHelper.create(this, 1f, DragHelperCallback())

    private var headerView: View? = null
    private var contentView: View? = null

    private var initialMotionX: Float = 0f
    private var initialMotionY: Float = 0f

    private var dragRange: Int = 0
    private var topOf: Int = 0
    private var dragOffset: Float = 0f
    private var firstLayout: Boolean = true

    var panelSlideListener: PanelSlideListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 2) {
            throw IllegalStateException()
        }
        this.headerView = getChildAt(0)
        this.contentView = getChildAt(1)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.firstLayout = true
    }

    private fun smoothSlideTo(slideOffset: Float): Boolean {
        val topBound = paddingTop
        if (dragRange != height - headerView!!.height) {
            dragRange = height - headerView!!.height
        }
        val y = topBound + slideOffset * this.dragRange
        if (this.dragHelper.smoothSlideViewTo(this.headerView!!, this.headerView!!.left, y.toInt())) {
            ViewCompat.postInvalidateOnAnimation(this)
            return true
        }
        return false
    }

    fun openPanel(): Boolean {
        panelSlideListener?.onPanelOpening(this.headerView!!)
        return smoothSlideTo(0f)
    }

    fun closePanel(): Boolean {
        panelSlideListener?.onPanelClosing(this.headerView!!)
        return smoothSlideTo(1f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dragRange = h - headerView!!.height
        if (h != oldh) {
            this.firstLayout = true
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        this.dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)

        this.dragRange = height - this.headerView!!.height
        this.headerView!!.layout(
                0,
                topOf,
                r,
                topOf + this.headerView!!.measuredHeight
        )
        this.contentView!!.layout(
                0,
                topOf + this.headerView!!.measuredHeight,
                r,
                topOf + b
        )

        if (this.firstLayout) {
            this.smoothSlideTo(1f)
            this.firstLayout = false
        }
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == headerView
        }

        override fun onViewDragStateChanged(state: Int) {
            if (dragHelper.viewDragState == ViewDragHelper.STATE_IDLE) {
                if (dragOffset <= 0) {
                    panelSlideListener?.onPanelOpened(headerView!!)
                } else {
                    panelSlideListener?.onPanelClosed(headerView!!)
                }
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            topOf = top
            dragOffset = (top / dragRange).toFloat()

            contentView!!.alpha = 1 - dragOffset

            requestLayout()
            panelSlideListener?.onPanelSlide(headerView!!, dragOffset)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var top = paddingTop
            if (yvel > 0 || dragOffset > 0.5f) {
                top += dragRange
            }
            dragHelper.settleCapturedViewAt(releasedChild.left, top)
            invalidate()
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return dragRange
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val topBound = paddingTop
            val bottomBound = height - headerView!!.height

            return min(max(top, topBound), bottomBound)
        }

    }

    override fun computeScroll() {
        if (this.dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)

        if (action != MotionEvent.ACTION_DOWN) {
            this.dragHelper.cancel()
            return super.onInterceptTouchEvent(ev)
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            this.dragHelper.cancel()
            return false
        }

        val x = ev.x
        val y = ev.y
        var interceptTap = false

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                initialMotionX = x
                initialMotionY = y
                interceptTap = this.dragHelper.isViewUnder(this.headerView, x.toInt(), y.toInt())
            }
            MotionEvent.ACTION_MOVE -> {
                val adx = abs(x - initialMotionX)
                val ady = abs(y - initialMotionY)
                val slop = this.dragHelper.touchSlop
                if (ady > slop && adx > ady) {
                    this.dragHelper.cancel()
                    return false
                }
            }
        }

        return this.dragHelper.shouldInterceptTouchEvent(ev) || interceptTap
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        this.dragHelper.processTouchEvent(ev)

        val action = ev.action
        val x = ev.x
        val y = ev.y

        var isHeaderViewUnder = this.dragHelper.isViewUnder(this.headerView, x.toInt(), y.toInt())

        when (action and MotionEventCompat.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                initialMotionX = x
                initialMotionY = y
            }
            MotionEvent.ACTION_UP -> {
                val dx = x - initialMotionX
                val dy = y - initialMotionY
                val slop = this.dragHelper.touchSlop
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (this.dragOffset <= 0f) {
                        closePanel()
                    } else {
                        openPanel()
                    }
                }
            }
        }

        return isHeaderViewUnder && isViewHit(this.headerView!!, x.toInt(), y.toInt()) || isViewHit(this.contentView!!, x.toInt(), y.toInt())
    }

    private fun isViewHit(view: View, x: Int, y: Int): Boolean {
        var viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        var parentLocation = IntArray(2)
        getLocationOnScreen(parentLocation)
        val sx = parentLocation[0] + x
        val sy = parentLocation[1] + y
        return sx >= viewLocation[0] && sx < viewLocation[0] + view.width && sy >= viewLocation[1] && sy < viewLocation[1] + view.height
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(View.resolveSizeAndState(maxWidth, widthMeasureSpec, 0), View.resolveSizeAndState(maxHeight, heightMeasureSpec, 0))
    }

    interface PanelSlideListener {

        fun onPanelSlide(panel: View, slideOffset: Float)

        fun onPanelOpening(panel: View)

        fun onPanelOpened(panel: View)

        fun onPanelClosing(panel: View)

        fun onPanelClosed(panel: View)

    }

}