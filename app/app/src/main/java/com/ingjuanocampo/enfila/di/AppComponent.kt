package com.ingjuanocampo.enfila.di

import com.ingjuanocampo.enfila.android.AppEnFila
import com.ingjuanocampo.enfila.android.state.LoggedStateImpl
import com.ingjuanocampo.enfila.android.state.NotLoggedImpl
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule
import com.ingjuanocampo.enfila.domain.state.AppState
import com.ingjuanocampo.enfila.domain.state.AppStateProvider

object AppComponent {

    private val notLoggedState by lazy { NotLoggedImpl(AppEnFila.context) }
    private val loggedState by lazy { LoggedStateImpl(AppEnFila.context) }
    private val appStateProvider by lazy { AppStateProvider(loggedState = loggedState, notLoggedState = notLoggedState,
    isLoggedMethod = DomainModule.provideIsUserLoggedMethod()) }

    fun providesState(): AppState = appStateProvider.provideCurrentState()

    fun provideSignUC() = DomainModule.provideSignUC(appStateProvider)

}