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
    private val originalPoint = Point()

    private var marginBottom = 0f
    private var histogramGapRatio = 5f
    private var histogramWidthRatio = 10f
    private var totalHeightRatio = 100f
    private val totalWidthRatio = histogramWidthRatio.times(7) + histogramGapRatio.times(8)
    private val histogramRatio = totalWidthRatio.div(totalHeightRatio)
    private val dayRegionMap = mutableMapOf<Int, Region>()
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
        val histogramWidth: Float
        val histogramHeight: Float
        val viewRatio = w.div(h.toFloat())
        // 绘制条形图 width < height
        if (viewRatio >= 1) {
            // View width >= height，绘制条形图的height与View的height一致
            histogramHeight = h.toFloat()
            histogramWidth = histogramRatio.times(histogramHeight)
            x = w.minus(histogramWidth).div(2)
            y = histogramHeight
        } else {
            // View width < height，绘制条形图的width与View的width一致
            histogramWidth = w.toFloat()
            histogramHeight = histogramWidth.div(histogramRatio)
            x = 0f
            y = h.minus(histogramHeight).div(2)
        }
        globalRegion.set(x.toInt(), y.minus(histogramHeight).toInt(), x.plus(histogramWidth).toInt(), y.toInt())
        originalPoint.set(x.toInt(), y.toInt())
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
            if (dayRegion.value.contains(x, y)) {
                return dayRegion.key
            }
        }
        return -1
    }

    private fun drawHistogramBackground(canvas: Canvas) {

    }

    private fun drawHistogram(canvas: Canvas) {

    }

    private fun drawBottomText(canvas: Canvas) {

    }
}