package com.zerdaket.chartpractice.widget.chart.histogram.model

import java.util.*

/**
 * @author zerdaket
 * @date 2020/8/8 11:14 PM
 */
data class HistogramData(
    val timestamp: Long,
    val value: Float
) {
    fun getDayofWeek() = Calendar.getInstance().run {
        timeInMillis = timestamp
        get(Calendar.DAY_OF_WEEK) - 1
    }
}