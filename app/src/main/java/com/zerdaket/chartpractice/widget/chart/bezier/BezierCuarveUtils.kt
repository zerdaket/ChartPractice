package com.zerdaket.chartpractice.widget.chart.bezier

import android.graphics.PointF

/**
 * @author zerdaket
 * @date 2020/10/30 10:45 PM
 */
object BezierCuarveUtils {

    fun calculateBezierPoint(controlPointList: List<PointF>, frame: Int): List<PointF> {

        val pointList = ArrayList<PointF>()

        if (controlPointList.isEmpty()) {
            return pointList
        } else if (controlPointList.size == 1) {
            return pointList.apply { add(controlPointList[0]) }
        }

        val delta = 1f / frame
        val k = controlPointList.size - 1

        var u = 0f
        while (u <= 1f) {
            u += delta
            pointList.add(PointF().toBezier(u, k, 0, controlPointList))
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
     */
    private fun PointF.toBezier(u: Float, k: Int, index: Int, controlPointList: List<PointF>): PointF = if (k == 1) {
        val point1 = controlPointList[index]
        val point2 = controlPointList[index + 1]
        PointF(
            (1f - u) * point1.x + u * point2.x,
            (1f - u) * point1.y + u * point2.y
        )
    } else {
        val point1 = toBezier(u, k - 1, index, controlPointList)
        val point2 = toBezier(u, k - 1, index + 1, controlPointList)
        PointF(
            (1f - u) * point1.x + u * point2.x,
            (1f - u) * point1.y + u * point2.y
        )
    }

}