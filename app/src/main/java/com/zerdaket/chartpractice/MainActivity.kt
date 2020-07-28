package com.zerdaket.chartpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zerdaket.chartpractice.view.chart.pie.model.PieData
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = ArrayList<PieData>()
        for (index in 0..3) {
            list.add(PieData(
                index,
                Random.Default.nextFloat()
            ))
        }
        pieChart.setData(list)
    }
}