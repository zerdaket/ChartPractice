package com.zerdaket.chartpractice

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_circlechart.*

/**
 * @author zerdaket
 * @date 2020/8/24 11:46 PM
 */
class CircleChartActivity : AppCompatActivity(R.layout.activity_circlechart) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    circle.setProgress(s.toString().toFloat())
                }
            }

        })
        start.setOnClickListener {
            circle.start()
        }
        reset.setOnClickListener {
            circle.reset()
        }
    }

}