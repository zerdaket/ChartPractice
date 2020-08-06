package com.zerdaket.chartpractice.widget.chart.polygon

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

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
    private val valuePaint = Paint()

    private val dataList = ArrayList<Float>()

    init {
        mainPaint.style = Paint.Style.STROKE
        mainPaint.color = Color.BLACK
        valuePaint.strokeWidth = 4f
        valuePaint.color = Color.GRAY
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
        val size = min(dataList.size, lines)
        valuePaint.alpha = 255
        valuePaint.style = Paint.Style.STROKE
        path.reset()
        for (i in 0 until size) {
            val value = min(dataList[i], maxValue)
            val ratio = value.div(maxValue)
            val r = ratio.times(radius)

            val x = cos(angle.times(i)).times(r).plus(centerPoint.x)
            val y = sin(angle.times(i)).times(r).plus(centerPoint.y)

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            canvas.drawCircle(x, y, 8f, valuePaint)
        }
        path.close()
        canvas.drawPath(path, valuePaint)
        valuePaint.alpha = 127
        valuePaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(path, valuePaint)
    }

    fun setData(data: List<Float>) {
        dataList.clear()
        dataList.addAll(data)
        invalidate()
    }
}