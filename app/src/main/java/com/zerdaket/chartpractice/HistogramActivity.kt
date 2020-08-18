package com.zerdaket.chartpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerdaket.chartpractice.widget.chart.histogram.model.HistogramData
import kotlinx.android.synthetic.main.activity_histogram.*

/**
 * @author zerdaket
 * @date 2020/8/16 11:51 PM
 */
class HistogramActivity: AppCompatActivity(R.layout.activity_histogram) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = ArrayList<HistogramData>()
        val timestamp = System.currentTimeMillis()
        for (index in 0..6) {
            val value = (1f..100f).start
            list.add(HistogramData(timestamp.times(index), value))
        }
        histogram.setData(list)
    }
}