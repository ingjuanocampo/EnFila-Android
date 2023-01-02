package com.ingjuanocampo.enfila.android.home.profile.viewmodel

import com.ingjuanocampo.enfila.android.home.profile.domain.ProfileCard

sealed class ProfileState {
    object LoadingProfileInfo: ProfileState()
    data class ProfileLoaded(val profileCard: ProfileCard): ProfileState()
    object LoggingOut: ProfileState()
    object LoggedOut: ProfileState()
}