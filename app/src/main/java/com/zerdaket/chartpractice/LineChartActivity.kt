package com.zerdaket.chartpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerdaket.chartpractice.widget.chart.line.model.LineData
import kotlinx.android.synthetic.main.activity_linechart.*

/**
 * @author zerdaket
 * @date 2020/9/25 11:46 PM
 */
class LineChartActivity: AppCompatActivity(R.layout.activity_linechart) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = mutableListOf(
            LineData(0, 20),
            LineData(1, 23),
            LineData(2, 35),
            LineData(3, 21),
            LineData(4, 40),
            LineData(5, 52),
            LineData(6, 44),
            LineData(7, 68),
            LineData(8, 48),
            LineData(9, 25),
            LineData(10, 25),
            LineData(11, 25),
            LineData(12, 60),
            LineData(13, 72),
            LineData(14, 56),
            LineData(15, 48),
            LineData(16, 68),
            LineData(17, 55),
            LineData(18, 80),
            LineData(19, 74),
            LineData(20, 63),
            LineData(21, 52),
            LineData(22, 40),
            LineData(23, 48),
            LineData(24, 45)
        )
        lineChart.setData(list)
    }
}