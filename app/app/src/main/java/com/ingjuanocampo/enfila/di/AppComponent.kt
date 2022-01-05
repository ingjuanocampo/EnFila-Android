package com.ingjuanocampo.enfila.di

import android.annotation.SuppressLint
import android.content.Context
import com.ingjuanocampo.enfila.android.AppEnFila
import com.ingjuanocampo.enfila.android.state.LoggedStateImpl
import com.ingjuanocampo.enfila.android.state.NotLoggedImpl
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule
import com.ingjuanocampo.enfila.domain.state.AppState
import com.ingjuanocampo.enfila.domain.state.AppStateProvider


object AppComponent {

    @SuppressLint("StaticFieldLeak")
    lateinit var domainModule: DomainModule

    fun init(context: Context) {
        domainModule = DomainModule(context)
    }

    private val notLoggedState by lazy { NotLoggedImpl(AppEnFila.context) }
    private val loggedState by lazy { LoggedStateImpl(AppEnFila.context) }
    private val appStateProvider by lazy { AppStateProvider(loggedState = loggedState, notLoggedState = notLoggedState,
    isLoggedMethod = domainModule.provideIsUserLoggedMethod()) }

    fun providesState(): AppState = appStateProvider.provideCurrentState()

    fun provideSignUC() = domainModule.provideSignUC(appStateProvider)

}