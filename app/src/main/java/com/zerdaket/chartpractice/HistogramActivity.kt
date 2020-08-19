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
        val array = arrayOf(12f, 15f, 6f, 28f, 42f, 30f, 61f)
        for (index in 0..6) {
            val value = array.random()
            list.add(HistogramData(timestamp.plus(index.times(86400000L)), value))
        }
        histogram.setData(list)
    }
}