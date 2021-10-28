package com.ingjuanocampo.enfila.android

import android.app.Application
import android.content.Context
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.Platform
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule

class AppEnFila: Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        DomainModule.appPlatform = Platform(this)
    }
}