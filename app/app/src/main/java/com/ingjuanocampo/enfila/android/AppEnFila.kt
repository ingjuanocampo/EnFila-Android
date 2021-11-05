package com.ingjuanocampo.enfila.android

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.ingjuanocampo.enfila.di.AppComponent

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