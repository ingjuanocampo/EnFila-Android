package com.ingjuanocampo.enfila.domain.usecases.signing

sealed class AuthState {
    object Authenticated: AuthState()
    data class NewAccount(val id: String): AuthState()
    data class AuthError(val e: Throwable): AuthState()
}