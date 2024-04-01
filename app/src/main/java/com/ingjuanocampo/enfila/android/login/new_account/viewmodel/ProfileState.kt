package com.ingjuanocampo.enfila.android.login.new_account.viewmodel

import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState

sealed class ProfileState {
    data class CreationFlow(val phone: String) : ProfileState()

    data class AuthProcess(val authState: AuthState) : ProfileState()
}
