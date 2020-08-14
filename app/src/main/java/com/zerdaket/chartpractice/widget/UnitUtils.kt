package com.zerdaket.chartpractice.widget

import com.zerdaket.chartpractice.App

/**
 * @author zerdaket
 * @date 2020/8/8 6:24 PM
 */

fun Float.dp2px(): Float = this.times(App.getContext().resources.displayMetrics.density)

fun Float.sp2px(): Float = this.times(App.getContext().resources.displayMetrics.scaledDensity)