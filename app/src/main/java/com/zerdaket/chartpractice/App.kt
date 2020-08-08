package com.zerdaket.chartpractice

import android.app.Application
import android.content.Context

/**
 * @author zerdaket
 * @date 2020/8/8 6:27 PM
 */
class App : Application() {

    companion object {
        private lateinit var context: Context
        fun getContext() = context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}