package com.zerdaket.chartpractice.widget.chart.histogram

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zerdaket.chartpractice.widget.chart.histogram.model.HistogramData
import com.zerdaket.chartpractice.widget.dp2px
import com.zerdaket.chartpractice.widget.sp2px
import kotlin.math.min

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

    private val marginBottom = 4f.dp2px()
    private var histogramGapRatio = 12f
    private var histogramWidthRatio = 8f
    private var totalHeightRatio = 120f
    private val totalWidthRatio = histogramWidthRatio.times(7) + histogramGapRatio.times(8)
    private val histogramRatio = totalWidthRatio.div(totalHeightRatio)
    private val dayRegionMap = mutableMapOf<Int, Pair<Path, Region>>()
    private val textPositions = arrayListOf<PointF>()
    private var currentSelectedDay = -1
    private val backgroundPathList = ArrayList<Path>()
    private val dataCollection = arrayOfNulls<HistogramData>(7)
    private val maxValue = 100f

    private val globalRegion = Region()

    init {
        textPaint.color = Color.GRAY
        textPaint.textSize = 10f.sp2px()
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
        textPositions.clear()
        backgroundPathList.clear()
        for (index in 0..6) {
            val left = histogramGap.times(index + 1) + histogramWidth.times(index) + x
            val top = y.minus(histogramHeight)
            val right = histogramGap.times(index + 1) + histogramWidth.times(index + 1) + x
            val bottom = y - textPaint.textSize.plus(marginBottom.times(2))
            val arcHeight = histogramHeight.times(0.08f)
            val path = Path()
            val histogramPath = Path()
            val region = Region()
            path.moveTo(left, bottom.minus(arcHeight))
            path.lineTo(left, top.plus(arcHeight))
            path.arcTo(left, top, right, top.plus(arcHeight), -180f, 180f, false)
            path.lineTo(right, bottom.minus(arcHeight))
            path.arcTo(left, bottom.minus(arcHeight), right, bottom, 0f, 180f, false)
            path.close()
            backgroundPathList.add(path)
            dataCollection[index]?.let {
                val height = histogramHeight.times(it.value.div(maxValue))
                val histogramTop = bottom.minus(arcHeight).minus(height)
                histogramPath.moveTo(left, bottom.minus(arcHeight))
                histogramPath.lineTo(left, histogramTop.plus(arcHeight))
                histogramPath.arcTo(left, histogramTop, right, histogramTop.plus(arcHeight), -180f, 180f, false)
                histogramPath.lineTo(right, bottom.minus(arcHeight))
                histogramPath.arcTo(left, bottom.minus(arcHeight), right, bottom, 0f, 180f, false)
                histogramPath.close()
                region.setPath(histogramPath, globalRegion)
                dayRegionMap[index] = Pair(histogramPath, region)
            }
            val textWidth = textPaint.measureText(week[index])
            val centerX = left + histogramWidth.div(2)
            val textX = centerX - textWidth.div(2)
            val textY = y.plus(marginBottom)
            textPositions.add(PointF(textX, textY))
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
                currentSelectedDay = getTouchedRegion(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                currentSelectedDay = getTouchedRegion(x, y)
            }
            MotionEvent.ACTION_UP -> {
                currentSelectedDay = getTouchedRegion(x, y)
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
        mainPaint.color = Color.LTGRAY
        for (path in backgroundPathList) {
            canvas.drawPath(path, mainPaint)
        }
    }

    private fun drawHistogram(canvas: Canvas) {
        for (dayRegion in dayRegionMap) {
            if (dayRegion.key == currentSelectedDay) {
                mainPaint.color = Color.YELLOW
            } else {
                mainPaint.color = Color.DKGRAY
            }
            canvas.drawPath(dayRegion.value.first, mainPaint)
        }
    }

    private fun drawBottomText(canvas: Canvas) {
        for (index in week.indices) {
            val pointF = textPositions[index]
            canvas.drawText(week[index], pointF.x, pointF.y, textPaint)
        }
    }

    fun setData(data: List<HistogramData>) {
        for (index in dataCollection.indices) {
            dataCollection[index] = null
        }
        val size = min(dataCollection.size, data.size)
        for (index in 0 until size) {
            dataCollection[data[index].getDayofWeek()] = data[index]
        }
        invalidate()
    }
}