package com.zerdaket.chartpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_circlechart.*

/**
 * @author zerdaket
 * @date 2020/8/24 11:46 PM
 */
class CircleChartActivity : AppCompatActivity(R.layout.activity_circlechart) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressEdt.addTextChangedListener(afterTextChanged = { text ->
            if (text.isNullOrEmpty()) {
                progressEdt.setText("0")
            }
        }, onTextChanged = { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                circle.setProgress(text.toString().toFloat())
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