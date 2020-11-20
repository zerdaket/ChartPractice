package com.zerdaket.chartpractice.widget.chart.bezier

import android.graphics.Path
import android.graphics.PointF

/**
 * @author zerdaket
 * @date 2020/10/30 10:45 PM
 */
object BezierCurveUtils {

    /**
     * frame: 帧数
     */
    fun getBezierPointList(controlPointList: List<PointF>, frame: Int = 1000): List<PointF> {

        val pointList = ArrayList<PointF>()

        if (frame == 0) {
            return pointList
        }

        if (controlPointList.size in 0..1) {
            controlPointList.forEach {
                pointList.add(it)
            }
            return pointList
        }

        val delta = 1f / frame
        // 最高阶数
        val k = controlPointList.size - 1

        var u = 0f
        while (u <= 1f) {
            u += delta
            pointList.add(PointF().deCasteljau(u, k, 0, controlPointList)) // 从起始点、最高阶数开始降阶
        }

        return pointList
    }

    /**
     * 公式解说：（p表示坐标点，后面的数字只是区分）
     * 场景：有一条线p1到p2，p0在中间，求p0的坐标
     *      p1◉--------○----------------◉p2
     *            u    p0
     *
     * 公式：p0 = p1+u*(p2-p1) 整理得出 p0 = (1-u)*p1+u*p2
     * u: 当前帧数下的比例 0<=u<=1
     * k: 当前阶数
     */
    private fun PointF.deCasteljau(
        u: Float,
        k: Int,
        index: Int,
        controlPointList: List<PointF>
    ): PointF = if (k == 1) {
        val point1 = controlPointList[index]
        val point2 = controlPointList[index + 1]
        PointF(
            (1f - u) * point1.x + u * point2.x,
            (1f - u) * point1.y + u * point2.y
        )
    } else {
        val point1 = deCasteljau(u, k - 1, index, controlPointList)
        val point2 = deCasteljau(u, k - 1, index + 1, controlPointList)
        PointF(
            (1f - u) * point1.x + u * point2.x,
            (1f - u) * point1.y + u * point2.y
        )
    }

    fun List<PointF>.toPath(): Path {
        val path = Path()
        forEachIndexed { index, pointF ->
            if (index == 0) {
                path.moveTo(pointF.x, pointF.y)
            } else {
                path.lineTo(pointF.x, pointF.y)
            }
        }
        return path
    }

}