package com.ingjuanocampo.enfila.domain.usecases.signing

import com.ingjuanocampo.common.composable.ViewEffect

sealed class AuthState: ViewEffect {
    object Authenticated : AuthState()
    data class NewAccount(val id: String) : AuthState()
    data class AuthError(val e: Throwable) : AuthState()
}
