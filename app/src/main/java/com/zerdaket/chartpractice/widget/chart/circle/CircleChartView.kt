package com.zerdaket.chartpractice.widget.chart.circle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
    private val progressPath = Path()
    private val centerPointF = PointF()
    private val rectF = RectF()
    private val progressAnimator = ValueAnimator.ofFloat(0f)
    private val maxValue = 100f

    init {
        mainPaint.isAntiAlias = true
        mainPaint.color = Color.LTGRAY
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeCap = Paint.Cap.ROUND
        progressAnimator.duration = 1000
        progressAnimator.interpolator = AccelerateDecelerateInterpolator()
        progressAnimator.addUpdateListener {
            doOnUpdate(it.animatedValue as Float)
        }
    }

    private fun doOnUpdate(value: Float) {
        progressPath.reset()
        rectF.set(centerPointF.x.minus(radius), centerPointF.y.minus(radius), centerPointF.x.plus(radius), centerPointF.y.plus(radius))
        val sweepAngle = value.div(maxValue).times(360f)
        progressPath.addArc(rectF, -90f, sweepAngle)
        postInvalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ringPath.reset()
        val min = min(w, h).toFloat()
        ringWidth = min.div(20)
        mainPaint.strokeWidth = ringWidth
        radius = min.div(2).minus(ringWidth.div(2))
        centerPointF.set(w.div(2f), h.div(2f))
        ringPath.addCircle(centerPointF.x, centerPointF.y, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRing(canvas)
        drawProgress(canvas)
    }

    private fun drawRing(canvas: Canvas) {
        mainPaint.color = Color.LTGRAY
        canvas.drawPath(ringPath, mainPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        mainPaint.color = Color.YELLOW
        canvas.drawPath(progressPath, mainPaint)
    }

    fun reset() {
        doOnUpdate(0f)
    }

    fun setProgress(progress: Float) {
        val value = progress.coerceIn(0f, 100f)
        progressAnimator.setFloatValues(value)
    }

    fun start() {
        progressAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator.cancel()
    }
}