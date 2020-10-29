package com.zerdaket.chartpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pieChartBtn.setOnClickListener {
            startActivity(Intent(this, PieChartActivity::class.java))
        }
        regularPolygonBtn.setOnClickListener {
            startActivity(Intent(this, RegularPolygonActivity::class.java))
        }
        histogramBtn.setOnClickListener {
            startActivity(Intent(this, HistogramActivity::class.java))
        }
        circleBtn.setOnClickListener {
            startActivity(Intent(this, CircleChartActivity::class.java))
        }
        lineChartBtn.setOnClickListener {
            startActivity(Intent(this, LineChartActivity::class.java))
        }
        bezierCurveBtn.setOnClickListener {
            startActivity(Intent(this, BezierCurveActivity::class.java))
        }
    }

}