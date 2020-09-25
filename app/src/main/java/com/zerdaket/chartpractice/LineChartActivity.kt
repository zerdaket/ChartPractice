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
            LineData(8, 12),
            LineData(9, 24),
        )
        lineChart.setData(list)
    }
}