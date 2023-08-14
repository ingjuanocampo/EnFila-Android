package com.ingjuanocampo.enfila.android

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

// #006e39 base color https://m3.material.io/theme-builder#/custom
// 00c1fe

@HiltAndroidApp
class AppEnFila : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
