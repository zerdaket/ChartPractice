package com.zerdaket.chartpractice.widget.chart.histogram

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zerdaket.chartpractice.widget.sp2px

/**
 * @author zerdaket
 * @date 2020/8/7 11:44 PM
 */
class HistogramView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {


    private val week = arrayOf("Sun.", "Mon.", "Tues.", "Wed.", "Thur.", "Fri.", "Sat.")
    private val textPaint = Paint()
    private val mainPaint = Paint()

    /**
     * 条形图原点
     */
    private val originalPointF = PointF()

    private var marginBottom = 0f
    private var histogramGapRatio = 8f
    private var histogramWidthRatio = 8f
    private var totalHeightRatio = 100f
    private val totalWidthRatio = histogramWidthRatio.times(7) + histogramGapRatio.times(8)
    private val histogramRatio = totalWidthRatio.div(totalHeightRatio)
    private val dayRegionMap = mutableMapOf<Int, Pair<Path, Region>>()
    private var currentSelectedDay = -1

    private val globalRegion = Region()

    init {
        textPaint.color = Color.GRAY
        textPaint.textSize = 12f.sp2px()
        textPaint.isAntiAlias = true
        mainPaint.color = Color.LTGRAY
        mainPaint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 绘制条形图居中显示，计算原点
        val x: Float
        val y: Float
        val histogramTotalWidth: Float
        val histogramHeight: Float
        val viewRatio = w.div(h.toFloat())
        if (viewRatio >= 1) {
            // View width >= height，绘制条形图的height与View的height一致
            histogramHeight = h.toFloat()
            histogramTotalWidth = histogramRatio.times(histogramHeight)
            x = w.minus(histogramTotalWidth).div(2)
            y = histogramHeight
        } else {
            // View width < height，绘制条形图的width与View的width一致
            histogramTotalWidth = w.toFloat()
            histogramHeight = histogramTotalWidth.div(histogramRatio)
            x = 0f
            y = h - h.minus(histogramHeight).div(2)
        }
        globalRegion.set(x.toInt(), y.minus(histogramHeight).toInt(), x.plus(histogramTotalWidth).toInt(), y.toInt())
        originalPointF.set(x, y)
        val histogramGap = histogramGapRatio.div(totalWidthRatio).times(histogramTotalWidth)
        val histogramWidth = histogramWidthRatio.div(totalWidthRatio).times(histogramTotalWidth)
        for (index in 0..6) {
            val left = histogramGap.times(index + 1) + histogramWidth.times(index) + x
            val top = y.minus(histogramHeight)
            val right = histogramGap.times(index + 1) + histogramWidth.times(index + 1) + x
            val bottom = y
            val arcHeight = histogramHeight.times(0.08f)
            val path = Path()
            val region = Region()
            path.moveTo(left, bottom.minus(arcHeight))
            path.lineTo(left, top.plus(arcHeight))
            path.arcTo(left, top, right, top.plus(arcHeight), -180f, 180f, false)
            path.lineTo(right, bottom.minus(arcHeight))
            path.arcTo(left, bottom.minus(arcHeight), right, bottom, 0f, 180f, false)
            path.close()
            region.setPath(path, globalRegion)
            dayRegionMap[index] = Pair(path, region)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHistogramBackground(canvas)
        drawHistogram(canvas)
        drawBottomText(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_CANCEL -> {

            }
        }
        invalidate()
        return true
    }

    private fun getTouchedRegion(x: Int, y: Int): Int {
        for (dayRegion in dayRegionMap) {
            if (dayRegion.value.second.contains(x, y)) {
                return dayRegion.key
            }
        }
        return -1
    }

    private fun drawHistogramBackground(canvas: Canvas) {
        for (dayMap in dayRegionMap) {
            canvas.drawPath(dayMap.value.first, mainPaint)
        }
    }

    private fun drawHistogram(canvas: Canvas) {

    }

    private fun drawBottomText(canvas: Canvas) {

    }
}