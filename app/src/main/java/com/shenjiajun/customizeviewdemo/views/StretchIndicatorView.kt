package com.shenjiajun.customizeviewdemo.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip

/**
 * Created by shenjiajun on 2018/8/27.
 */
class StretchIndicatorView : View {

    var pageNum = 0
        set(value) {
            field = value
            postInvalidate()
        }
    var currentPage = 0
        set(value) {
            field = value
            postInvalidate()
        }

    var selectColor = Color.WHITE
        set(value) {
            field = value
            init()
            postInvalidate()
        }

    var unSelectColor = Color.GRAY
        set(value) {
            field = value
            init()
            postInvalidate()
        }
    var radius = context.dip(5).toFloat()
        set(value) {
            field = value
            init()
            postInvalidate()
        }
    var indicatorPadding = context.dip(5)
        set(value) {
            field = value
            init()
            postInvalidate()
        }

    var showMiddleLine = true
        set(value) {
            field = value
            postInvalidate()
        }

    private lateinit var selectPaint: Paint
    private lateinit var unselectPaint: Paint

    private var viewHeight: Int = 0
    private var viewWidth: Int = 0


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        selectPaint = Paint()
        selectPaint.isAntiAlias = (true)
        selectPaint.isDither = (true)
        selectPaint.color = (selectColor)
//        selectPaint.strokeWidth = (radius)
        selectPaint.style = (Paint.Style.FILL)

        unselectPaint = Paint()
        unselectPaint.isAntiAlias = (true)
        unselectPaint.isDither = (true)
        unselectPaint.color = (unSelectColor)
//        unselectPaint.strokeWidth = (radius)
        unselectPaint.style = (Paint.Style.FILL)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewWidth = measuredWidth
        viewHeight = measuredHeight

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)


        var indicatorHeight = (radius * 2 + paddingBottom + paddingTop).toInt()
        var indicatorWidth = (pageNum * (radius * 2 + indicatorPadding) - indicatorPadding + paddingRight + paddingLeft).toInt()

        if (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize)
        } else if (widthMode == View.MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, indicatorHeight)
        } else if (heightMode == View.MeasureSpec.EXACTLY) {
            setMeasuredDimension(indicatorWidth, heightSize)
        } else {
            setMeasuredDimension(indicatorWidth, indicatorHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until pageNum) {
            if (i <= currentPage) {
                canvas.drawCircle(i * (radius * 2 + indicatorPadding) + radius, radius, radius, selectPaint)
                if (showMiddleLine && i >= 1) {
                    canvas.drawRect(((i - 1) * (radius * 2 + indicatorPadding) + radius),
                            0f,
                            (i * (radius * 2 + indicatorPadding) + radius),
                            radius * 2,
                            selectPaint)
                }
            } else {
                canvas.drawCircle(i * (radius * 2 + indicatorPadding) + radius, radius, radius, unselectPaint)
            }
        }
    }
}