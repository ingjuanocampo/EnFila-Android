package com.ingjuanocampo.enfila.di

import com.enfila.data.messaging.TwillioCredentials
import com.ingjuanocampo.enfila.android.state.LoggedStateImpl
import com.ingjuanocampo.enfila.android.state.NotLoggedImpl
import com.ingjuanocampo.enfila.domain.TwillioCredentialsImpl
import com.ingjuanocampo.enfila.domain.state.states.LoggedState
import com.ingjuanocampo.enfila.domain.state.states.NotLoggedState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AppComponent {

    @Binds
    fun bindsNotLoggedImpl(notLoggedImpl: NotLoggedImpl): NotLoggedState

    @Binds
    fun bindsLoggedImpl(loggedState: LoggedStateImpl): LoggedState

    @Binds
    fun bindsCredentials(twillioCredentials: TwillioCredentialsImpl): TwillioCredentials
}
