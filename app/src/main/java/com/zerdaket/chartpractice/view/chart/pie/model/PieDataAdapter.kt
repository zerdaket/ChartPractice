package com.zerdaket.chartpractice.view.chart.pie.model

import android.graphics.Color

/**
 * @author zerdaket
 * @date 2020/7/29 12:18 AM
 */
object PieDataAdapter {

    fun getColor(type: Int) = when (type) {
        0 -> Color.BLACK
        1 -> Color.DKGRAY
        2 -> Color.LTGRAY
        3 -> Color.GRAY
        else -> Color.TRANSPARENT
    }

}