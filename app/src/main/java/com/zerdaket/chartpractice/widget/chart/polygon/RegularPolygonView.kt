package com.zerdaket.chartpractice.widget.chart.polygon

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * @author zerdaket
 * @date 2020/8/1 11:32 PM
 */
class RegularPolygonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    /**
     * 外接圆半径
     */
    private var radius = 0f

    private var maxValue = 6f

    private var valueStep = 1.0f // maxValue % valueStep == 0

    private var lines = 6
    private val angle = PI.times(2).div(lines).toFloat()

    /**
     * 中心点
     */
    private val centerPoint = Point()

    /**
     * 绘制路径
     */
    private val path = Path()

    private val mainPaint = Paint()

    init {
        mainPaint.style = Paint.Style.STROKE
        mainPaint.color = Color.BLACK
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = min(w, h).div(2f)
        centerPoint.set(w.div(2), h.div(2))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPolygon(canvas)
        drawLines(canvas)
        drawRegion(canvas)
    }

    private fun drawPolygon(canvas: Canvas) {
        val radiusStep = valueStep.div(maxValue).times(radius)
        val count = maxValue.div(valueStep).toInt()
        for (i in 1..count) {
            path.reset()
            val curRadius = radiusStep.times(i)
            for (j in 0..lines) {
                if (j == 0) {
                    path.moveTo(centerPoint.x.plus(curRadius), centerPoint.y.toFloat())
                } else {
                    val x = cos(angle.times(j)).times(curRadius).plus(centerPoint.x)
                    val y = sin(angle.times(j)).times(curRadius).plus(centerPoint.y)
                    path.lineTo(x, y)
                }
            }
            path.close()
            canvas.drawPath(path, mainPaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        for (i in 0..lines) {
            path.reset()
            path.moveTo(centerPoint.x.toFloat(), centerPoint.y.toFloat())
            val x = cos(angle.times(i)).times(radius).plus(centerPoint.x)
            val y = sin(angle.times(i)).times(radius).plus(centerPoint.y)
            path.lineTo(x, y)
            canvas.drawPath(path, mainPaint)
        }
    }

    private fun drawRegion(canvas: Canvas) {

    }
}