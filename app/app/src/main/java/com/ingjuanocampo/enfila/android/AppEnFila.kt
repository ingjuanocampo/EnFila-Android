package com.ingjuanocampo.enfila.android

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.ingjuanocampo.enfila.di.AppComponent

// #006e39 base color https://m3.material.io/theme-builder#/custom
// 00c1fe

class AppEnFila : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        AppComponent.init(this)
    }
}