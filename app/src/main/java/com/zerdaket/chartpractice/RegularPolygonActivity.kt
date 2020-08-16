package com.zerdaket.chartpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_regular_polygon.*

/**
 * @author zerdaket
 * @date 2020/8/16 11:51 PM
 */
class RegularPolygonActivity: AppCompatActivity(R.layout.activity_regular_polygon) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        polygon.setData(mutableListOf(3.0f, 2.0f, 2.0f, 4.0f, 1.0f, 6.0f))
    }
}