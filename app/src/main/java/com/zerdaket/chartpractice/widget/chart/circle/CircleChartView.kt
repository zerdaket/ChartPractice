package com.zerdaket.chartpractice.widget.chart.circle

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * @author zerdaket
 * @date 2020/8/20 11:34 PM
 */
class CircleChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private var ringWidth = 0f
    private var radius = 0f

    private val mainPaint = Paint()
    private val textPaint = Paint()
    private val ringPath = Path()
    private val centerPointF = PointF()

    init {
        mainPaint.isAntiAlias = true
        mainPaint.color = Color.LTGRAY
        mainPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ringPath.reset()
        val min = min(w, h).toFloat()
        ringWidth = min.div(20)
        mainPaint.strokeWidth = ringWidth
        radius = min.div(2).minus(ringWidth.div(2))
        centerPointF.set(x.div(2f), y.div(2f))
        ringPath.addCircle(centerPointF.x, centerPointF.y, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRing(canvas)
    }

    private fun drawRing(canvas: Canvas) {
        canvas.drawPath(ringPath, mainPaint)
    }


}