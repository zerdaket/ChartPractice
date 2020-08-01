package com.zerdaket.chartpractice.widget.chart.polygon

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * @author zerdaket
 * @date 2020/8/1 11:32 PM
 */
class RegularPolygonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun drawPolygon(canvas: Canvas) {

    }

    private fun drawLines(canvas: Canvas) {

    }

    private fun drawRegion(canvas: Canvas) {

    }
}