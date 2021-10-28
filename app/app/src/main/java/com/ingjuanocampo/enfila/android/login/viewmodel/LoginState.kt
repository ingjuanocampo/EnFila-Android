package com.ingjuanocampo.enfila.android.login.viewmodel

import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState

sealed class LoginState {
    object NumberSet: LoginState()
    object ToVerifyCode: LoginState()
    data class AuthenticationProcessState(val authState: AuthState): LoginState()
}