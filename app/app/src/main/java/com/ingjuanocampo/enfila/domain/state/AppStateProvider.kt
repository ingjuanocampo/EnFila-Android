package com.ingjuanocampo.enfila.domain.state

import com.ingjuanocampo.enfila.domain.state.states.LoggedState
import com.ingjuanocampo.enfila.domain.state.states.NotLoggedState
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateProvider @Inject constructor(
    private val loggedState: LoggedState,
    private val notLoggedState: NotLoggedState,
    private val userRepository: UserRepository,
) {

    private var currentState: AppState = if (userRepository.isUserLogged()) loggedState else notLoggedState

    fun provideCurrentState(): AppState {
        if (userRepository.isUserLogged()) {
            toLoggedState()
        } else {
            toNotLoggedState()
        }
        return currentState
    }

    fun toNotLoggedState() {
        currentState = notLoggedState
    }

    fun toLoggedState() {
        currentState = loggedState
    }


}