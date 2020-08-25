package com.zerdaket.chartpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_circlechart.*

/**
 * @author zerdaket
 * @date 2020/8/24 11:46 PM
 */
class CircleChartActivity: AppCompatActivity(R.layout.activity_circlechart) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start.setOnClickListener {
            circle.start()
        }
    }

}