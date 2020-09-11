package com.zerdaket.chartpractice.widget.chart.line

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * @author zerdaket
 * @date 2020/9/10 11:35 PM
 */
class LineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val linePath = Path()
    private val textPaint = Paint()
    private val mainPaint = Paint()
    private val bounds = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(paddingStart.toFloat(), paddingTop.toFloat(), w.minus(paddingEnd.toFloat()), h.minus(paddingBottom.toFloat()))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas) {

    }

    private fun drawLines(canvas: Canvas) {

    }

    private fun drawText(canvas: Canvas) {

    }

}