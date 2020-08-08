package com.zerdaket.chartpractice.widget.chart.histogram

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zerdaket.chartpractice.widget.sp2px

/**
 * @author zerdaket
 * @date 2020/8/7 11:44 PM
 */
class HistogramView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val week = arrayOf("Sun.", "Mon.", "Tues.", "Wed.", "Thur.", "Fri.", "Sat.")
    private val textPaint = Paint()

    init {
        textPaint.color = Color.GRAY
        textPaint.textSize = 12f.sp2px()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}