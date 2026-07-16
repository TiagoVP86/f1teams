package com.example.formula1

import android.app.Application
import android.content.Context
import com.example.formula1.di.AppContainer

class App : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

val Context.appContainer: AppContainer
    get() = (applicationContext as App).container
