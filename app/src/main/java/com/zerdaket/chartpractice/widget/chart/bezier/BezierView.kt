package com.zerdaket.chartpractice.widget.chart.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zerdaket.chartpractice.widget.chart.bezier.BezierCurveUtils.toPath
import com.zerdaket.chartpractice.widget.dp2px

/**
 * @author zerdaket
 * @date 2020/11/20 11:34 PM
 */
class BezierView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr)  {

    private val mainPaint = Paint()
    private val pathA = Path()
    private lateinit var pathB: Path

    init {
        mainPaint.isAntiAlias = true
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeWidth = 2f.dp2px()
        mainPaint.color = Color.RED
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pathA.moveTo(100f, 100f)
        pathA.cubicTo(150f, 200f, 200f, 150f, 350f, 200f)

        val list = arrayListOf(
            PointF(100f, 400f),
            PointF(150f, 500f),
            PointF(200f, 450f),
            PointF(350f, 500f)
        )
        pathB = BezierCurveUtils.getBezierPointList(list).toPath()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(pathA, mainPaint)
        canvas.drawPath(pathB, mainPaint)
    }

}