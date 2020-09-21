package com.zerdaket.chartpractice.widget.chart.line

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zerdaket.chartpractice.widget.dp2px

/**
 * @author zerdaket
 * @date 2020/9/10 11:35 PM
 */
class LineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val time = arrayOf(0, 6, 12, 18)
    private val verticalScale = arrayOf(0, 25, 50, 75, 100)

    private val linePath = Path()
    private val textPaint = Paint()
    private val mainPaint = Paint()
    private val bounds = RectF()
    private val chartBounds = RectF()

    init {
        textPaint.isAntiAlias = true
        textPaint.textSize = 12f.dp2px()
        textPaint.color = Color.GRAY
        mainPaint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(paddingStart.toFloat(), paddingTop.toFloat(), w.minus(paddingEnd.toFloat()), h.minus(paddingBottom.toFloat()))
        val chartMarginStart = textPaint.measureText(0.toString())
        val chartMarginEnd = textPaint.measureText(100.toString()) + 4f.dp2px()
        val chartMarginTop = 12f.dp2px().div(2) + 4f.dp2px()
        val chartMarginBottom = 12f.dp2px() + 8f.dp2px()
        // TODO 兼容控件宽高比字体小的情况
        chartBounds.set(bounds.left + chartMarginStart, bounds.top + chartMarginTop, bounds.right - chartMarginEnd, bounds.bottom - chartMarginBottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawText(canvas)
        drawLines(canvas)
    }

    private fun drawBackground(canvas: Canvas) {

    }

    private fun drawLines(canvas: Canvas) {

    }

    private fun drawText(canvas: Canvas) {

    }

}