package com.zerdaket.chartpractice.widget.chart.pie

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zerdaket.chartpractice.widget.chart.pie.model.PieData
import com.zerdaket.chartpractice.widget.chart.pie.model.PieDataAdapter
import kotlin.math.min

/**
 * @author zerdaket
 * @date 2020/7/28 11:51 PM
 */
class PieChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var startAngle = 0f
    private val gapAngle = 2f
    private var r = 0
    private val bounds = Rect()
    private var sumValue = 0f
    private val dataCollection = ArrayList<PieData>()
    private var touchedType = -1
    private var currentType = -1
    private val dataTypeMap = mutableMapOf<Int, Pair<Path, Region>>()
    private val globalRegion = Region()

    init {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    /**
     * 确定绘制扇形的路径和点击的区域
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val x = w.div(2)
        val y = h.div(2)
        r = min(w, h).div(2)
        bounds.set(x.minus(r), y.minus(r), x.plus(r), y.plus(r))
        globalRegion.set(bounds)
        dataTypeMap.clear()

        var currentAngle = startAngle
        for (data in dataCollection) {
            val angle = data.value.div(sumValue).times(360).minus(gapAngle)
            val path = Path()
            path.addArc(RectF(bounds), currentAngle, angle)
            path.lineTo(x.toFloat(), y.toFloat())
            path.close()
            val region = Region()
            region.setPath(path, globalRegion)
            dataTypeMap[data.type] = Pair(path, region)
            currentAngle += angle.plus(gapAngle)
        }
    }

    /**
     * 根据扇形不同的状态绘制
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (data in dataCollection) {
            val pair = dataTypeMap[data.type]
            pair?.let {
                paint.color = if (currentType == data.type) Color.YELLOW else PieDataAdapter.getColor(data.type)
                canvas.drawPath(pair.first, paint)
            }
        }
    }

    /**
     * 记录被点击的区域
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchedType = getTouchedRegion(x, y)
                currentType = touchedType
            }
            MotionEvent.ACTION_MOVE -> {
                currentType = getTouchedRegion(x, y)
            }
            MotionEvent.ACTION_UP -> {
                currentType = getTouchedRegion(x, y)
                if (currentType == touchedType && currentType != -1) {

                }
                touchedType = -1
                currentType = -1
            }
            MotionEvent.ACTION_CANCEL -> {
                touchedType = -1
                currentType = -1
            }
        }
        invalidate()
        return true
    }

    private fun getTouchedRegion(x: Int, y: Int): Int {
        for (dataType in dataTypeMap) {
            if (dataType.value.second.contains(x, y)) {
                return dataType.key
            }
        }
        return -1
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