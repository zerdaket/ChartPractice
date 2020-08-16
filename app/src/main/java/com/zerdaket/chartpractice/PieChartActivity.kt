package com.zerdaket.chartpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerdaket.chartpractice.widget.chart.pie.model.PieData
import kotlinx.android.synthetic.main.activity_piechart.*
import kotlin.random.Random

/**
 * @author zerdaket
 * @date 2020/8/16 11:50 PM
 */
class PieChartActivity: AppCompatActivity(R.layout.activity_piechart) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = ArrayList<PieData>()
        for (index in 0..3) {
            list.add(
                PieData(
                index,
                Random.nextFloat()
            )
            )
        }
        pieChart.setData(list)
    }
}