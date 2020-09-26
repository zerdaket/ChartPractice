package com.zerdaket.chartpractice.widget.chart.line

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zerdaket.chartpractice.widget.chart.line.model.LineData
import com.zerdaket.chartpractice.widget.dp2px

/**
 * @author zerdaket
 * @date 2020/9/10 11:35 PM
 */
class LineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val time = arrayOf(0, 6, 12, 18)
    private val verticalScale = arrayOf(0, 25, 50, 75, 100)
    private val maxValue = 100

    private val linePath = Path()
    private val textPaint = Paint()
    private val mainPaint = Paint()
    private val bounds = RectF()
    private val chartBounds = RectF()

    private val bottomTextPositionMap = mutableMapOf<Int, PointF>()
    private val verticalTextPositionMap = mutableMapOf<Int, PointF>()

    private val lineDataList: MutableList<LineData> = mutableListOf()

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
        val chartMarginEnd = textPaint.measureText(100.toString()) + 8f.dp2px()
        val chartMarginTop = 12f.dp2px().div(2) + 4f.dp2px()
        val chartMarginBottom = 12f.dp2px() + 8f.dp2px()
        // TODO 兼容控件宽高比字体小的情况
        chartBounds.set(bounds.left + chartMarginStart, bounds.top + chartMarginTop, bounds.right - chartMarginEnd, bounds.bottom - chartMarginBottom)
        calculateBottomTextPosition()
        calculateVerticalTextPosition()
    }

    private fun calculateBottomTextPosition() {
        val y = chartBounds.bottom + 4f.dp2px() + 12f.dp2px()
        val gap = chartBounds.width().div(time.size)
        for (index in time.indices) {
            val text = time[index]
            val offset = textPaint.measureText(text.toString()).div(2f)
            val x = chartBounds.left + gap.times(index) - offset
            bottomTextPositionMap[text] = PointF(x, y)
        }
    }

    private fun calculateVerticalTextPosition() {
        val x = chartBounds.right + 4f.dp2px()
        val gap = chartBounds.height().div(verticalScale.size - 1)
        for (index in verticalScale.indices) {
            if (index == 0) {
                continue
            }
            val text = verticalScale[index]
            val offset = 12f.dp2px().div(2)
            val y = chartBounds.bottom - gap.times(index) + offset
            verticalTextPositionMap[text] = PointF(x, y)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawText(canvas)
        drawLines(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        mainPaint.color = Color.BLACK
        verticalScale.forEach {
            val startX = chartBounds.left
            val startY = chartBounds.bottom - it.div(maxValue.toFloat()).times(chartBounds.height())
            val endX = chartBounds.right
            val endY = startY
            canvas.drawLine(startX, startY, endX, endY, mainPaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        val xGap = chartBounds.width().div(24)
        val yGap = chartBounds.height().div(100)
        lineDataList.forEachIndexed { index, lineData ->
            val x = xGap.times(lineData.hour)
            val y = yGap.times(lineData.realValue)
            if (index == 0) {
                linePath.moveTo(x, y)
                return@forEachIndexed
            }
            linePath.lineTo(x, y)
        }
        mainPaint.color = Color.YELLOW
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeWidth = 2f.dp2px()
        canvas.drawPath(linePath, mainPaint)
    }

    private fun drawText(canvas: Canvas) {
        bottomTextPositionMap.forEach {
            canvas.drawText(it.key.toString(), it.value.x, it.value.y, textPaint)
        }

        verticalTextPositionMap.forEach {
            canvas.drawText(it.key.toString(), it.value.x, it.value.y, textPaint)
        }
    }

    fun setData(list: List<LineData>) {
        lineDataList.clear()
        lineDataList.addAll(list)
        invalidate()
    }

}