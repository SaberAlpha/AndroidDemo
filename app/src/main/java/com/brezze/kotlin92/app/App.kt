package com.brezze.kotlin92.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.brezze.library_common.app.CommonApp

class App :Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        CommonApp.setApplication(this, true)
    }
}