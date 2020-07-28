package com.zerdaket.chartpractice.view.chart.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.zerdaket.chartpractice.view.chart.pie.model.PieData
import com.zerdaket.chartpractice.view.chart.pie.model.PieDataAdapter
import kotlin.math.min

/**
 * @author zerdaket
 * @date 2020/7/28 11:51 PM
 */
class PieChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var startAngle = 0f
    private var chartWidth = 0
    private var chartHeight = 0
    private var r = 0f
    private var rectF = RectF()
    private var sumValue = 0f
    private val dataCollection = ArrayList<PieData>()

    init {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartWidth = w
        chartHeight = h
        val x = chartWidth.div(2)
        val y = chartHeight.div(2)
        r = min(chartWidth, chartHeight).div(2f)
        rectF = RectF(x.minus(r), y.minus(r), x.plus(r), y.plus(r))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var currentAngle = startAngle

        for (data in dataCollection) {
            paint.color = PieDataAdapter.getColor(data.type)
            val angle = data.value.div(sumValue).times(360)
            canvas.drawArc(rectF, currentAngle, angle, true, paint)
            currentAngle += angle
        }
    }

    fun setData(list: List<PieData>) {
        dataCollection.clear()
        sumValue = 0f
        dataCollection.addAll(list)
        for (data in dataCollection) {
            sumValue += data.value
        }
        invalidate()
    }
}