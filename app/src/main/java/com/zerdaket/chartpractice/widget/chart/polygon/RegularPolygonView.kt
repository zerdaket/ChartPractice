package com.zerdaket.chartpractice.widget.chart.polygon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * @author zerdaket
 * @date 2020/8/1 11:32 PM
 */
class RegularPolygonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    /**
     * 外接圆半径
     */
    private var radius = 0

    /**
     * 中心点
     */
    private val centerPoint = Point()

    /**
     * 绘制路径
     */
    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = min(w, h).div(2)
        centerPoint.set(w.div(2), h.div(2))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPolygon(canvas)
        drawLines(canvas)
        drawRegion(canvas)
    }

    private fun drawPolygon(canvas: Canvas) {
        path.reset()
    }

    private fun drawLines(canvas: Canvas) {

    }

    private fun drawRegion(canvas: Canvas) {

    }
}