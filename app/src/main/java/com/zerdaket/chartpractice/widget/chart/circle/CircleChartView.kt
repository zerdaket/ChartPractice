package com.zerdaket.chartpractice.widget.chart.circle

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import androidx.core.animation.doOnEnd
import com.zerdaket.chartpractice.widget.dp2px
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author zerdaket
 * @date 2020/8/20 11:34 PM
 */
class CircleChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private var targetCount = 8000
    private var currentCount = 0

    private val bounds = RectF()
    private val centerPointF = PointF()
    private var ringStrokeWidth = 0f
    private var ringRadius = 0f
    private val ringBounds = RectF()
    private var backgroundStrokeWidth = 0f
    private var backgroundRadius = 0f
    private var circleRadius = 0f
    private val circlePointF = PointF()
    private val contentBounds = RectF()
    private val originalContentBounds = RectF()
    private val textBounds = Rect()
    private var textLength = 0
    private var textContentWidth = 0f
    private val tickBounds = RectF()
    private val originalTickBounds = RectF()
    private val tickAspectRatio = 4 / 3f
    private var tickPercent = 0f

    private val backgroundPath = Path()
    private val progressPath = Path()
    private val tickPath = Path()
    private val dstTickPath = Path()

    private val progressPathMeasure = PathMeasure()
    private val tickPathMeasure = PathMeasure()

    private val backgroundPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#2EFFFFFF")
            style = Paint.Style.STROKE
        }
    }
    private val mainPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }
    private val circlePaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.FILL
        }
    }
    private val circleBackgroundPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.FILL
        }
    }
    private val textPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
        }
    }
    private val tickPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
    }

    private lateinit var linearGradient: Shader
    private val startColor = Color.parseColor("#8BE4EC")
    private val endColor = Color.parseColor("#53C4D5")

    private val progressInterpolator = PathInterpolator(0.33f, 0.3f, 0.2f, 1f)
    private val scaleInterpolator = PathInterpolator(0.3f, 0.977f, 0.32f, 1f)
    private val linearInterpolator = LinearInterpolator()
    private var progressAnimator: ValueAnimator? = null
    private var numberProgressAnimator: ValueAnimator? = null
    private var numberRemainProgressAnimator: ValueAnimator? = null
    private var numberDisappearingAnimator: ValueAnimator? = null
    private var tickShowingAnimator: ValueAnimator? = null
    private var tickScaleAnimator: ValueAnimator? = null
    private var numberScaleAnimator: ValueAnimator? = null
    private var numberShowingAnimator: ValueAnimator? = null
    private var tickDisappearingAnimator: ValueAnimator? = null
    private var tickShrinkAnimator: ValueAnimator? = null

    private var beforeTargetAnimator = AnimatorSet()
    private var tickAnimator = AnimatorSet()
    private var afterTargetAnimator = AnimatorSet()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (w - paddingRight).toFloat(),
            (h - paddingBottom).toFloat()
        )
        val outerRadius = min(bounds.width(), bounds.height()) / 2f
        centerPointF.set(bounds.width() / 2f, bounds.height() / 2f)

        ringStrokeWidth = 0.16f * outerRadius
        ringRadius = outerRadius - ringStrokeWidth / 2f
        ringBounds.set(
            centerPointF.x - ringRadius,
            centerPointF.y - ringRadius,
            centerPointF.x + ringRadius,
            centerPointF.y + ringRadius
        )
        mainPaint.strokeWidth = ringStrokeWidth

        backgroundStrokeWidth = 0.16f * outerRadius
        backgroundRadius = outerRadius - backgroundStrokeWidth / 2f
        backgroundPaint.strokeWidth = backgroundStrokeWidth
        backgroundPath.reset()
        backgroundPath.addCircle(centerPointF.x, centerPointF.y, backgroundRadius, Path.Direction.CW)

        circleRadius = 0.065f * outerRadius
        circlePointF.set(centerPointF.x, centerPointF.y - ringRadius)

        val innerRadius = ringRadius - ringStrokeWidth / 2f
        val halfTextHeight = innerRadius * 0.45f
        val halfTextWidth = sqrt(innerRadius.pow(2) - halfTextHeight.pow(2))
        originalContentBounds.set(
            centerPointF.x - halfTextWidth,
            centerPointF.y - halfTextHeight,
            centerPointF.x + halfTextWidth,
            centerPointF.y + halfTextHeight
        )
        contentBounds.set(originalContentBounds)

        val halfTickHeight = innerRadius * 0.4f
        val halfTickWidth = halfTickHeight * tickAspectRatio
        originalTickBounds.set(
            centerPointF.x - halfTickWidth,
            centerPointF.y - halfTickHeight,
            centerPointF.x + halfTickWidth,
            centerPointF.y + halfTickHeight
        )
        tickBounds.set(originalTickBounds)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawProgress(canvas)
        drawProgressText(canvas)
        drawTick(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawPath(backgroundPath, backgroundPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        canvas.drawPath(progressPath, mainPaint)
        if (currentCount == 0) {
            canvas.drawCircle(centerPointF.x, centerPointF.y - ringRadius, 0.5f * ringStrokeWidth, circleBackgroundPaint)
        }
        updateCircleColor()
        canvas.drawCircle(circlePointF.x, circlePointF.y, circleRadius, circlePaint)
    }

    private fun updateCircleColor() {
        linearGradient = LinearGradient(
            circlePointF.x,
            circlePointF.y - circleRadius,
            circlePointF.x,
            circlePointF.y + circleRadius,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
        circlePaint.shader = linearGradient
    }

    private fun drawProgressText(canvas: Canvas) {
        val text = "$currentCount"
        val textMarginStartEnd = contentBounds.width() * 0.05f
        var textSize = contentBounds.height()

        val needRecalculate = (text.length != textLength || textContentWidth == 0f) && !afterTargetAnimator.isRunning
        if (needRecalculate) {
            do {
                textSize -= 2f.dp2px()
                textPaint.textSize = textSize
                val textWidth = textPaint.measureText(text, 0, text.length)
                textContentWidth = textWidth + textMarginStartEnd * 2
            } while (textContentWidth > contentBounds.width())
            textLength = text.length
        }

        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val x = contentBounds.left + contentBounds.width() / 2f
        val y = contentBounds.bottom - contentBounds.height() / 2f + textBounds.height() / 2f

        canvas.drawText(text, 0, text.length, x, y, textPaint)
    }

    private fun drawTick(canvas: Canvas) {
        tickPath.reset()
        val startX = tickBounds.left
        val startY = tickBounds.bottom - tickBounds.height() / 3f
        val joinX = tickBounds.left + tickBounds.width() / 3f
        val joinY = tickBounds.bottom
        val endX = tickBounds.right
        val endY = tickBounds.top
        tickPath.apply {
            moveTo(startX, startY)
            lineTo(joinX, joinY)
            moveTo(endX, endY)
        }
        tickPathMeasure.setPath(tickPath, false)

        dstTickPath.reset()
        tickPathMeasure.getSegment(0f, tickPercent * tickPathMeasure.length, dstTickPath, true)
        canvas.drawPath(dstTickPath, tickPaint)
    }

    fun setValue(current: Int, target: Int) {
        if (isRunning()) {
            return
        }
        targetCount = max(2000, target)
        val value = max(0, current)

        progressPath.reset()
        currentCount = value
        val percent = min(value.toFloat() / targetCount, 1f)
        val sweepAngle = percent * 360f
        progressPath.addArc(ringBounds, -90f, sweepAngle)
        progressPathMeasure.setPath(progressPath, false)
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        progressPathMeasure.getPosTan(progressPathMeasure.length, pos, tan)
        if (pos[0] == 0f || pos[1] == 0f) {
            circlePointF.set(centerPointF.x, centerPointF.y - ringRadius)
        } else {
            circlePointF.set(pos[0], pos[1])
        }
        invalidate()
    }

    fun setValueWithAnimation(current: Int, target: Int) {
        if (isRunning()) {
            return
        }
        targetCount = max(2000, target)
        resetAnimator()
        setupAnimator(max(0, current), target)
    }

    private fun resetAnimator() {
        textPaint.alpha = 255
        circlePaint.alpha = 255
        beforeTargetAnimator = AnimatorSet()
        tickAnimator = AnimatorSet()
        afterTargetAnimator = AnimatorSet()
    }

    private fun setupAnimator(current: Int, target: Int) {
        val remain = current - target
        when {
            remain < 0 -> {
                undone(current)
            }
            remain == 0 -> {
                done(target)
            }
            remain > 0 -> {
                above(target, current)
            }
        }
    }

    private fun undone(current: Int) {
        progressAnimator = ValueAnimator.ofInt(0, current).apply {
            duration = 1500
            interpolator = progressInterpolator
            addUpdateListener {
                doOnProgress(it.animatedValue as Int)
            }
        }
        numberProgressAnimator = ValueAnimator.ofInt(0, current).apply {
            duration = 1500
            interpolator = linearInterpolator
            addUpdateListener {
                currentCount = it.animatedValue as Int
                invalidate()
            }
        }
        beforeTargetAnimator.play(progressAnimator).with(numberProgressAnimator)
        beforeTargetAnimator.start()
    }

    private fun done(target: Int) {
        var finalTextSize = 0f
        progressAnimator = ValueAnimator.ofInt(0, target).apply {
            duration = 1500
            interpolator = progressInterpolator
            addUpdateListener {
                doOnProgress(it.animatedValue as Int)
            }
        }
        numberProgressAnimator = ValueAnimator.ofInt(0, target).apply {
            duration = 1500
            interpolator = linearInterpolator
            addUpdateListener {
                currentCount = it.animatedValue as Int
                invalidate()
            }
        }
        beforeTargetAnimator.play(progressAnimator).with(numberProgressAnimator)

        numberDisappearingAnimator = ValueAnimator.ofInt(255, 0).apply {
            duration = 150
            interpolator = linearInterpolator
            addUpdateListener {
                val alpha = it.animatedValue as Int
                textPaint.alpha = alpha
                circlePaint.alpha = alpha
            }
        }
        tickShowingAnimator = ValueAnimator.ofInt(0, 255).apply {
            duration = 150
            interpolator = linearInterpolator
            addUpdateListener {
                tickPaint.alpha = it.animatedValue as Int
            }
        }
        tickScaleAnimator = ValueAnimator.ofFloat(0.6f, 1.2f, 1f).apply {
            duration = 500
            interpolator = scaleInterpolator
            addUpdateListener {
                originalTickBounds.scale(it.animatedValue as Float, tickBounds)
                invalidate()
            }
        }
        tickAnimator.play(numberDisappearingAnimator).with(tickShowingAnimator).with(tickScaleAnimator)
        tickAnimator.doOnEnd {
            finalTextSize = textPaint.textSize
        }

        numberScaleAnimator = ValueAnimator.ofFloat(0.85f, 1f).apply {
            duration = 500
            interpolator = scaleInterpolator
            addUpdateListener {
                val factor = it.animatedValue as Float
                textPaint.textSize = finalTextSize * factor
                invalidate()
            }
        }
        numberShowingAnimator = ValueAnimator.ofInt(0, 255).apply {
            duration = 200
            interpolator = linearInterpolator
            addUpdateListener {
                val alpha = it.animatedValue as Int
                if (alpha == 0) {
                    textPaint.textSize = finalTextSize * 0.85f
                }
                textPaint.alpha = alpha
                circlePaint.alpha = alpha
            }
        }
        tickShrinkAnimator = ValueAnimator.ofFloat(1f, 0.85f).apply {
            duration = 500
            interpolator = scaleInterpolator
            addUpdateListener {
                originalTickBounds.scale(it.animatedValue as Float, tickBounds)
            }
        }
        tickDisappearingAnimator = ValueAnimator.ofInt(255, 0).apply {
            duration = 200
            interpolator = linearInterpolator
            addUpdateListener {
                tickPaint.alpha = it.animatedValue as Int
            }
        }
        afterTargetAnimator.play(numberScaleAnimator).with(numberShowingAnimator).with(tickShrinkAnimator).with(tickDisappearingAnimator)
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(beforeTargetAnimator, tickAnimator, afterTargetAnimator)
        animatorSet.start()
    }

    private fun above(target: Int, current: Int) {
        var finalTextSize = 0f
        progressAnimator = ValueAnimator.ofInt(0, target).apply {
            duration = 1500
            interpolator = progressInterpolator
            addUpdateListener {
                doOnProgress(it.animatedValue as Int)
            }
        }
        numberProgressAnimator = ValueAnimator.ofInt(0, target).apply {
            duration = 1500
            interpolator = linearInterpolator
            addUpdateListener {
                currentCount = it.animatedValue as Int
                invalidate()
            }
        }
        numberRemainProgressAnimator = ValueAnimator.ofInt(target, current).apply {
            duration = 500
            interpolator = linearInterpolator
            addUpdateListener {
                currentCount = it.animatedValue as Int
                invalidate()
            }
        }
        beforeTargetAnimator.play(progressAnimator).with(numberProgressAnimator)
        beforeTargetAnimator.play(numberRemainProgressAnimator).after(numberProgressAnimator)

        numberDisappearingAnimator = ValueAnimator.ofInt(255, 0).apply {
            duration = 150
            interpolator = linearInterpolator
            addUpdateListener {
                val alpha = it.animatedValue as Int
                textPaint.alpha = alpha
                circlePaint.alpha = alpha
            }
        }
        tickShowingAnimator = ValueAnimator.ofInt(0, 255).apply {
            duration = 150
            interpolator = linearInterpolator
            addUpdateListener {
                tickPaint.alpha = it.animatedValue as Int
            }
        }
        tickScaleAnimator = ValueAnimator.ofFloat(0.6f, 1.2f, 1f).apply {
            duration = 500
            interpolator = scaleInterpolator
            addUpdateListener {
                originalTickBounds.scale(it.animatedValue as Float, tickBounds)
                invalidate()
            }
        }
        tickAnimator.play(numberDisappearingAnimator).with(tickShowingAnimator).with(tickScaleAnimator)
        tickAnimator.doOnEnd {
            finalTextSize = textPaint.textSize
        }

        numberScaleAnimator = ValueAnimator.ofFloat(0.85f, 1f).apply {
            duration = 500
            interpolator = scaleInterpolator
            addUpdateListener {
                val factor = it.animatedValue as Float
                textPaint.textSize = finalTextSize * factor
                invalidate()
            }
        }
        numberShowingAnimator = ValueAnimator.ofInt(0, 255).apply {
            duration = 200
            interpolator = linearInterpolator
            addUpdateListener {
                val alpha = it.animatedValue as Int
                if (alpha == 0) {
                    textPaint.textSize = finalTextSize * 0.85f
                }
                textPaint.alpha = alpha
                circlePaint.alpha = alpha
            }
        }
        afterTargetAnimator.play(numberScaleAnimator).with(numberShowingAnimator)
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(beforeTargetAnimator, tickAnimator, afterTargetAnimator)
        animatorSet.start()
    }

    private fun doOnProgress(value: Int) {
        progressPath.reset()
        currentCount = value
        val percent = min(value.toFloat() / targetCount, 1f)
        val sweepAngle = percent * 360f
        progressPath.addArc(ringBounds, -90f, sweepAngle)
        progressPathMeasure.setPath(progressPath, false)
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        progressPathMeasure.getPosTan(progressPathMeasure.length, pos, tan)
        if (pos[0] == 0f || pos[1] == 0f) {
            circlePointF.set(centerPointF.x, centerPointF.y - ringRadius)
        } else {
            circlePointF.set(pos[0], pos[1])
        }
    }

    private fun RectF.scale(factor: Float, dst: RectF) {
        val oldWidth = width()
        val oldHeight = height()
        val rectCenterX = left + oldWidth / 2f
        val rectCenterY = top + oldHeight / 2f
        val newWidth = oldWidth * factor
        val newHeight = oldHeight * factor
        dst.set(
            rectCenterX - newWidth / 2f,
            rectCenterY - newHeight / 2f,
            rectCenterX + newWidth / 2f,
            rectCenterY + newHeight / 2f
        )
    }

    fun isRunning() = beforeTargetAnimator.isRunning || tickAnimator.isRunning || afterTargetAnimator.isRunning

}