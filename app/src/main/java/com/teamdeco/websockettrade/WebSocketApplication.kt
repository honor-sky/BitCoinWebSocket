package com.teamdeco.websockettrade

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WebSocketApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}