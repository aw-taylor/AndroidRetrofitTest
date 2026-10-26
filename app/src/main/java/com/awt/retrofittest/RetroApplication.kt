package com.awt.retrofittest

import android.app.Application
import com.awt.retrofittest.data.AppContainer
import com.awt.retrofittest.data.DefaultAppContainer

class RetroApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}