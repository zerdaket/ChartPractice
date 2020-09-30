package com.zerdaket.chartpractice.widget.chart.circle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.text.NumberFormat
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author zerdaket
 * @date 2020/8/20 11:34 PM
 */
class CircleChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private var backgroundStrokeWidth = 0f
    private var progressStrokeWidth = 0f
    private var circleRadius = 0f
    private var radius = 0f

    private lateinit var linearGradient: LinearGradient
    private val progressStartColor = Color.parseColor("#d09693")
    private val progressEndColor = Color.parseColor("#c71d6f")
    private val backgroundPaint = Paint()
    private val mainPaint = Paint()
    private val textPaint = Paint()
    private val backgroundPath = Path()
    private val progressPath = Path()
    private val circlePath = Path()
    private val centerPointF = PointF()
    private val bounds = RectF()
    private val progressBounds = RectF()
    private val textBounds = RectF()
    private val progressAnimator = ValueAnimator.ofFloat(0f)
    private val targetValue = 100f
    private var percent = 0f
    private val numberFormatter = NumberFormat.getInstance()

    private var progressChangedListener: ProgressChangedListener? = null

    init {
        numberFormatter.maximumFractionDigits = 1
        backgroundPaint.isAntiAlias = true
        backgroundPaint.color = Color.LTGRAY
        backgroundPaint.style = Paint.Style.STROKE

        mainPaint.isAntiAlias = true
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeCap = Paint.Cap.ROUND
        textPaint.isAntiAlias = true
        textPaint.color = Color.BLACK

        progressAnimator.duration = 1000
        progressAnimator.interpolator = AccelerateDecelerateInterpolator()
        progressAnimator.addUpdateListener {
            doOnUpdate(it.animatedValue as Float)
        }
    }

    /**
     * 更新进度同时绘制View
     */
    private fun doOnUpdate(value: Float) {
        progressPath.reset()
        percent = value.div(targetValue)
        val sweepAngle = percent.times(360f)
        progressPath.addArc(progressBounds, -90f, sweepAngle)
        progressChangedListener?.onChanged(percent.times(100f))
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(paddingLeft.toFloat(), paddingTop.toFloat(), (w - paddingRight).toFloat(), (h - paddingBottom).toFloat())
        val outerRadius = min(bounds.width(), bounds.height()).div(2f)
        progressStrokeWidth = outerRadius.times(0.25f)
        backgroundStrokeWidth = outerRadius.times(0.2f)
        radius = outerRadius - progressStrokeWidth.div(2f)
        centerPointF.set(bounds.width().div(2f), bounds.height().div(2f))
        linearGradient = LinearGradient(
            centerPointF.x.minus(outerRadius),
            centerPointF.y.minus(outerRadius),
            centerPointF.x.plus(outerRadius),
            centerPointF.y.plus(outerRadius),
            progressStartColor, progressEndColor, Shader.TileMode.CLAMP
        )

        progressBounds.set(centerPointF.x.minus(radius), centerPointF.y.minus(radius), centerPointF.x.plus(radius), centerPointF.y.plus(radius))

        backgroundPath.reset()
        backgroundPath.addCircle(centerPointF.x, centerPointF.y, radius, Path.Direction.CW)

        circleRadius = outerRadius.times(0.08f)
        circlePath.reset()
        circlePath.addCircle(centerPointF.x, centerPointF.y - radius, circleRadius, Path.Direction.CW)

        val innerRadius = outerRadius - progressStrokeWidth
        val halfTextHeight = innerRadius.times(0.4f)
        val halfTextWidth = sqrt(innerRadius.pow(2) - halfTextHeight.pow(2))
        textBounds.set(centerPointF.x - halfTextWidth, centerPointF.y - halfTextHeight, centerPointF.x + halfTextWidth, centerPointF.y + halfTextHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawProgress(canvas)
        drawProgressText(canvas)
    }

    /**
     * 绘制圆环背景
     */
    private fun drawBackground(canvas: Canvas) {
        backgroundPaint.strokeWidth = backgroundStrokeWidth
        canvas.drawPath(backgroundPath, backgroundPaint)
    }

    /**
     * 绘制圆环进度
     */
    private fun drawProgress(canvas: Canvas) {
        mainPaint.shader = linearGradient
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeWidth = progressStrokeWidth
        canvas.drawPath(progressPath, mainPaint)
        if (percent > 0f) {
            mainPaint.color = Color.WHITE
            mainPaint.shader = null
            mainPaint.style = Paint.Style.FILL
            canvas.drawPath(circlePath, mainPaint)
        }
    }

    private fun drawProgressText(canvas: Canvas) {
        val text = "${numberFormatter.format(percent * 100)}%"
        val textMarginStartEnd = textBounds.width().times(0.15f)
        val textSize = textBounds.height().times(0.6f)
        val percentSize = textSize.times(0.55f)
        textPaint.textSize = textSize
        val textWidth = textPaint.measureText(text, 0, text.length - 1)
        textPaint.textSize = percentSize
        val percentWidth = textPaint.measureText(text, text.length - 1, text.length)
        val totalWidth = textWidth + percentWidth + textMarginStartEnd * 2

        textPaint.textSize = textSize
        val x = textBounds.left + textBounds.width().minus(totalWidth).div(2f).plus(textMarginStartEnd)
        val y = textBounds.bottom - textBounds.height().minus(textSize).div(2f).plus(textPaint.fontMetrics.bottom)

        canvas.drawText(text, 0, text.length - 1, x, y, textPaint)
        textPaint.textSize = percentSize
        canvas.drawText(text, text.length - 1, text.length, x + textWidth, y, textPaint)
    }

    /**
     * 重置圆环进度
     */
    fun reset() {
        doOnUpdate(0f)
    }

    /**
     * 设置进度
     */
    fun setProgress(progress: Float) {
        val value = progress.coerceIn(0f, 100f)
        progressAnimator.setFloatValues(value)
    }

    fun setProgressChangedListener(owner: LifecycleOwner, listener: ProgressChangedListener) {
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }
        progressChangedListener = listener
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                    progressChangedListener = null
                    owner.lifecycle.removeObserver(this)
                }
            }

        })
    }

    /**
     * 开始执行动画
     */
    fun start() {
        progressAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator.cancel()
    }
}

interface ProgressChangedListener {
    fun onChanged(progress: Float)
}