package com.yueban.textcapturedemo

import android.app.Application
import android.content.Context

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
            private set
    }
}
