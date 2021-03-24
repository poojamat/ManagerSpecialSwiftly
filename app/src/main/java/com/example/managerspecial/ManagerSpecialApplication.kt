package com.example.managerspecial

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ManagerSpecialApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ManagerSpecialApplication)
            modules(networkModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}