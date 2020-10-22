package com.zerdaket.chartpractice.widget.chart.line

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zerdaket.chartpractice.widget.chart.line.model.LineData
import com.zerdaket.chartpractice.widget.dp2px
import kotlin.math.max

/**
 * @author zerdaket
 * @date 2020/9/10 11:35 PM
 */
class LineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val time = arrayOf(0, 6, 12, 18)
    private val verticalScale = arrayOf(0, 25, 50, 75, 100)
    private val maxValue = 100

    private val linePath = Path()
    private val shaderPath = Path()
    private val textPaint = Paint()
    private val mainPaint = Paint()
    private val bounds = RectF()
    private val chartBounds = RectF()

    private val lineChartStartColor = Color.parseColor("#80feada6")
    private val lineChartEndColor = Color.parseColor("#1Af5efef")

    private val bottomTextPositionMap = mutableMapOf<Int, PointF>()
    private val verticalTextPositionMap = mutableMapOf<Int, PointF>()

    private val lineDataList: MutableList<LineData> = mutableListOf()
    private val maxLineData = LineData(0, 0)

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
        mainPaint.reset()
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
        mainPaint.reset()
        val xGap = chartBounds.width().div(24)
        val yGap = chartBounds.height().div(100)
        lineDataList.forEachIndexed { index, lineData ->
            val x = xGap.times(lineData.hour) + 6f.dp2px()
            val y = chartBounds.bottom - yGap.times(lineData.realValue)
            if (index == 0) {
                linePath.moveTo(x, y)
                shaderPath.moveTo(x, chartBounds.bottom)
                shaderPath.lineTo(x, y)
                return@forEachIndexed
            }
            linePath.lineTo(x, y)
            shaderPath.lineTo(x, y)
            if (index == lineDataList.lastIndex) {
                shaderPath.lineTo(x, chartBounds.bottom)
                shaderPath.close()
            }
        }
        mainPaint.color = lineChartStartColor
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeWidth = 2f.dp2px()
        canvas.drawPath(linePath, mainPaint)
        mainPaint.color = Color.WHITE
        mainPaint.style = Paint.Style.FILL
        val shader = LinearGradient(0f, 0f, 0f, chartBounds.bottom, lineChartStartColor, lineChartEndColor, Shader.TileMode.CLAMP)
        mainPaint.shader = shader
        canvas.drawPath(shaderPath, mainPaint)
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
        val map = mutableMapOf<Int, LineData>()
        var maxValue = 0
        lineDataList.forEach {
            maxValue = max(it.realValue, maxValue)
            map[it.realValue] = it
        }
        map[maxValue]?.let {
            maxLineData.hour = it.hour
            maxLineData.realValue = it.realValue
        }
        invalidate()
    }

}